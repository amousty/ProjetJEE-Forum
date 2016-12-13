package be.forum.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import be.forum.pojo.ActualitePOJO;
import be.forum.sgbd.Sprocs;
import oracle.jdbc.OracleTypes;

public class ActualiteDAO extends DAO<ActualitePOJO> {

	public ActualiteDAO(Connection conn) { super(conn); }

	@Override
	public void create(ActualitePOJO actualitePOJO) {
		CallableStatement cst = null;
		try {
			//Appel de la proc�dure stock�e pour cr�er une actualit�
			cst = connect.prepareCall(Sprocs.INSERTACTUALITE);

			cst.setString	(1, actualitePOJO.getTitre());
			cst.setString	(2, actualitePOJO.getDescription());
			cst.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (cst != null) {
				try {
					cst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void delete(ActualitePOJO actualitePOJO) {
		CallableStatement cst = null;
		try {
			//Appel de la proc�dure stock�e pour supprimer une actualit�
			cst = connect.prepareCall(Sprocs.DELETEACTUALITE);

			cst.setString(1, actualitePOJO.getTitre());
			cst.setString(2, actualitePOJO.getDescription());
			cst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (cst != null) {
				try {
					cst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void update(ActualitePOJO actualitePOJO) {
		CallableStatement cst = null;
		try {
			//Appel de la proc�dure stock�e pour modifier une actualite
			cst = connect.prepareCall(Sprocs.UPDATEACTUALITE);
			
			cst.setInt		(1, actualitePOJO.getID());
			cst.setString	(2, actualitePOJO.getTitre());
			cst.setString	(3, actualitePOJO.getDescription());
			cst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (cst != null) {
				try {
					cst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public ActualitePOJO find(int id) {
		ActualitePOJO 	 	 actualitePOJO 	= null;
		CallableStatement cst = null;
		
		try {
			//Appel de la proc�dure stock�e pour trouver une actualite
			cst = connect.prepareCall(Sprocs.SELECTACTUALITE);
			//pst = this.connect.prepareStatement("SELECT * FROM Actualite WHERE idActualite = ?");
			//J'ins�re le param�tre entrant
			cst.setInt(1, id);
			//Je r�cup�re les param�tres sortants de la proc�dures stock�es
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.registerOutParameter(3, java.sql.Types.VARCHAR);
			cst.executeQuery();
			
			actualitePOJO = new ActualitePOJO(
					id,
					cst.getString 	(2),
					cst.getString	(3)
			);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (cst != null) {
				try {
					cst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return actualitePOJO;
	}

	@Override
	public ArrayList<ActualitePOJO> getList() {
		ActualitePOJO 				actualitePOJO 		= null;
		CallableStatement 			cst 				= null;
		ResultSet 					rs 					= null;
		ArrayList<ActualitePOJO>	listActualitePOJO 	= new ArrayList<ActualitePOJO>();
		try {
			cst = connect.prepareCall(Sprocs.GETLISTACTUALITE);

			cst.registerOutParameter(1, OracleTypes.CURSOR);
			cst.executeUpdate();

			// On r�cup�re le curseur et on le cast � ResultSet
			rs = (ResultSet) cst.getObject(1);
			while (rs.next()) {
				actualitePOJO = new ActualitePOJO(
							rs.getInt			("idActualite"), 
							rs.getString		("titre"),
							rs.getString		("description")
						);
				listActualitePOJO.add(actualitePOJO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (cst != null) {
				try {
					cst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return listActualitePOJO;
	}

}
