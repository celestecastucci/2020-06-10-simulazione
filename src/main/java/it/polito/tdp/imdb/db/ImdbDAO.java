package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public void listAllActors(Map<Integer,Actor>idMap){
		String sql = "SELECT * FROM actors";
		//List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("id"))) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				idMap.put(actor.getId(), actor);
				}
			}
			conn.close();
			//return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			//return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public List<String> getGeneriTendina(){
		String sql="SELECT distinct m.genre "
				+ "FROM movies_genres m "
				+ "ORDER BY m.genre";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				
				result.add(res.getString("m.genre"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Actor> getVertici(String genere, Map<Integer,Actor>idMap){
		String sql="SELECT distinct a.id as attore_id "
				+ "FROM movies_genres mg, actors a, roles r, movies m "
				+ "WHERE a.id=r.actor_id AND r.movie_id=m.id AND m.id=mg.movie_id AND mg.genre=? ";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(idMap.get(res.getInt("attore_id")));
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Adiacenza> getAdiacenze(String genere, Map<Integer,Actor>idMap){
		String sql="SELECT  a1.id AS id1, a2.id AS id2, COUNT(mg1.movie_id) AS peso "
				+ "FROM movies_genres mg1, movies_genres mg2, actors a1,actors a2 , roles r1, roles r2, movies m1, movies m2 "
				+ "WHERE a1.id < a2.id AND  a1.id=r1.actor_id AND  a2.id= r2.actor_id AND "
				+ " r1.movie_id=m1.id AND r2.movie_id=m2.id AND m1.id=mg1.movie_id AND m2.id=mg2.movie_id AND mg1.movie_id=mg2.movie_id "
				+ " AND mg1.genre= mg2.genre AND mg1.genre=? "
				+ "GROUP BY a1.id, a2.id ";
		
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor a1= idMap.get(res.getInt("id1"));
				Actor a2= idMap.get(res.getInt("id2"));
				if(a1!=null && a2!=null) {
					Adiacenza a = new Adiacenza(a1, a2, res.getDouble("peso"));
					result.add(a);
				} else {
					System.out.println("errore get adiacenze");
				}
				
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
