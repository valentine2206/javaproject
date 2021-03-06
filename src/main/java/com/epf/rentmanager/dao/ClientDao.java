package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.persistence.ConnectionManager;

import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {

	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String UPDATE_CLIENT_QUERY = "UPDATE Client SET nom = ?, prenom = ?, email = ?, naissance = ? WHERE id = ?;";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	private static final String FIND_CLIENTS_COUNTS_QUERY = "SELECT COUNT(id) AS count FROM Client";
	
	public long create(Client client) throws DaoException {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement stmt = conn.prepareStatement(CREATE_CLIENT_QUERY,
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, client.getNom());
			stmt.setString(2, client.getPrenom());
			stmt.setString(3, client.getEmail());
			stmt.setDate(4, Date.valueOf(client.getNaissance()));
			long key = stmt.executeUpdate();
			conn.close();
			return key;
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public long update(Client client) throws DaoException {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement stmt = conn.prepareStatement(UPDATE_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, client.getNom());
			stmt.setString(2, client.getPrenom());
			stmt.setString(3, client.getEmail());
			stmt.setDate(4, Date.valueOf(client.getNaissance()));
			stmt.setInt(5, client.getId());
			long key = stmt.executeUpdate();
			conn.close();
			return key;
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public long delete(Client client) throws DaoException {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement stmt = conn.prepareStatement(DELETE_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, client.getId());
			long key = stmt.executeUpdate();
			conn.close();
			return key;
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public Optional<Client> findById(long id) throws DaoException {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement stmt = conn.prepareStatement(FIND_CLIENT_QUERY);
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			rs.next();

			Client client = new Client((int)id, rs.getString("nom"), rs.getString("prenom"),
					rs.getString("email"),
					rs.getDate("naissance").toLocalDate());
			return Optional.of(client);
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public int findCountClient() throws DaoException {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement stmt = conn.prepareStatement(FIND_CLIENTS_COUNTS_QUERY);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			int count = rs.getInt("count");
			return count;

		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public ArrayList<Client> findAll() throws DaoException {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement stmt = conn.prepareStatement(FIND_CLIENTS_QUERY);
			ResultSet rs = stmt.executeQuery();
			ArrayList<Client> clientResultList = new ArrayList<Client>();
			while (rs.next()) {
				Client client = new Client(rs.getInt("id"),
						rs.getString("nom"),
						rs.getString("prenom"),
						rs.getString("email"),
						rs.getDate("naissance").toLocalDate());

				clientResultList.add(client);
			}
			conn.close();
			return clientResultList;
		} catch (SQLException e) {
			throw new DaoException();
		}
	}
}
