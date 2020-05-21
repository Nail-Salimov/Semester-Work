package server.bl.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import server.entities.user.model.Role;
import server.entities.user.model.State;
import server.entities.user.model.UserDataModel;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_ADD_USER = "INSERT INTO user_data (mail, hash_password, name, role, state, address) values (?, ?, ?, ?, ?, ?)";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM user_data WHERE id = ?";
    private static final String SQL_FIND_BY_MAIL = "SELECT * FROM user_data WHERE mail = ?";
    private static final String SQL_CONFIRM_USER = "UPDATE user_data SET state = 'CONFIRMED' WHERE mail = ?";

    private static final String SQL_UPDATE_NAME_BY_MAIL = "UPDATE user_data SET name = ? WHERE mail = ?";
    private static final String SQL_UPDATE_ADDRESS_BY_MAIL = "UPDATE user_data SET address = ? WHERE mail =?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<UserDataModel> userRowMapper = (row, rowNumber) ->
            UserDataModel.builder()
                    .id(row.getLong("id"))
                    .name(row.getString("name"))
                    .mail(row.getString("mail"))
                    .hashPassword(row.getString("hash_password"))
                    .role(Role.valueOf(row.getString("role")))
                    .state(State.valueOf(row.getString("state")))
                    .address(row.getString("address"))
                    .build();


    @Override
    public Optional<UserDataModel> find(Long id) {
        try {
            UserDataModel user = jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{id}, userRowMapper);
            return Optional.of(user);
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }

    }

    @Override
    public List<UserDataModel> findAll() {
        return null;
    }

    @Override
    public UserDataModel save(UserDataModel entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_ADD_USER);
            statement.setString(1, entity.getMail());
            statement.setString(2, entity.getHashPassword());
            statement.setString(3, entity.getName());
            statement.setString(4, entity.getRole().toString());
            statement.setString(5, entity.getState().toString());
            statement.setString(6, entity.getAddress());
            return statement;
        }, keyHolder);
        return entity;
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public Optional<UserDataModel> findById(Long id) {
        return find(id);
    }

    @Override
    public Optional<UserDataModel> findByMail(String mail) {
        try {
            UserDataModel user = jdbcTemplate.queryForObject(SQL_FIND_BY_MAIL, new Object[]{mail}, userRowMapper);
            return Optional.of(user);
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public void confirmUser(String mail) {
        try {
            jdbcTemplate.update(SQL_CONFIRM_USER, mail);
        }catch (EmptyResultDataAccessException e){
            throw  new IllegalStateException("not confirmed");
        }
    }

    @Override
    public void changeName(String newName, String mail) {
        jdbcTemplate.update(SQL_UPDATE_NAME_BY_MAIL, newName, mail);
    }

    @Override
    public void changeAddress(String newAddress, String mail) {
        jdbcTemplate.update(SQL_UPDATE_ADDRESS_BY_MAIL, newAddress, mail);
    }
}
