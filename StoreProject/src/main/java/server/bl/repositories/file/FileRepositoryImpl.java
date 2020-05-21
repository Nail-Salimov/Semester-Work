package server.bl.repositories.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import server.entities.file.model.FileModel;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
public class FileRepositoryImpl implements FileRepository {

    private static final String SQL_SELECT_BY_ORIGINAL_NAME = "SELECT * FROM file_map WHERE original_name = ?";
    private static final String SQL_SELECT_BY_STORAGE_NAME = "SELECT * FROM file_map WHERE storage_name = ?";

    private static final String SQL_INSERT_FILE = "INSERT INTO file_map(original_name, storage_name, user_id, size) values (?, ?, ?, ?)";

    private static final String SQL_DELETE_BY_ORIGINAL_NAME = "DELETE FROM file_map WHERE original_name = ?";
    private static final String SQL_DELETE_BY_STORAGE_NAME = "DELETE FROM file_map WHERE storage_name = ?";

    private static final String SQL_SELECT_ALL = "SELECT * FROM file_map";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<FileModel> fileRowMapper = (row, rowNumber) ->
            FileModel.builder()
                    .id(row.getLong("id"))
                    .originalName(row.getString("original_name"))
                    .storageName(row.getString("storage_name"))
                    .size(row.getLong("size"))
                    .build();

    @Override
    public Optional<FileModel> findByOriginalName(String originalName) {
        try {
            FileModel file = jdbcTemplate.queryForObject(SQL_SELECT_BY_ORIGINAL_NAME, new Object[]{originalName}, fileRowMapper);
            return Optional.of(file);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<FileModel> findByStorageName(String storageName) {
        try {
            FileModel file = jdbcTemplate.queryForObject(SQL_SELECT_BY_STORAGE_NAME, new Object[]{storageName}, fileRowMapper);
            return Optional.of(file);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteByOriginalName(String originalName) {
        try {
            if (jdbcTemplate.update(SQL_DELETE_BY_ORIGINAL_NAME, originalName) == 0) {
                throw new IllegalArgumentException("no one line update");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void deleteByStorageName(String storageName) {
        try {
            if (jdbcTemplate.update(SQL_DELETE_BY_STORAGE_NAME, storageName) == 0) {
                throw new IllegalArgumentException("no one line update");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Optional<FileModel> find(String s) {
        throw new IllegalStateException("metHod not allowed");
    }

    @Override
    public List<FileModel> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, fileRowMapper);
    }

    @Override
    public FileModel save(FileModel entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_FILE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getOriginalName());
            statement.setString(2, entity.getStorageName());
            statement.setLong(3, entity.getUserDataModel().getId());
            statement.setLong(4, entity.getSize());
            return statement;
        }, keyHolder);

        Object o = keyHolder.getKeyList().get(0).get("id");
        Integer id = (Integer) o;
        entity.setId(id.longValue());
        return entity;
    }

    @Override
    public void delete(String s) {
        deleteByOriginalName(s);
    }
}
