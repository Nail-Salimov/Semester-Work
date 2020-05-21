package server.addition.loader;

import org.springframework.web.multipart.MultipartFile;
import server.entities.file.model.FileModel;
import server.entities.user.model.UserDataModel;

import java.io.File;

public interface FileLoader {

    FileModel uploadFile(MultipartFile file, UserDataModel userDto);
    File downloadFile(String storageName);
}
