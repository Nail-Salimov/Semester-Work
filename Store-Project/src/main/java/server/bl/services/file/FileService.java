package server.bl.services.file;

import org.springframework.web.multipart.MultipartFile;
import server.entities.file.dto.FileDto;
import server.entities.user.dto.UserDataDto;
import server.entities.user.model.UserDataModel;

import java.io.File;
import java.util.List;
import java.util.Optional;


public interface FileService {
     FileDto saveFile(MultipartFile file, UserDataModel userDataModel);
     FileDto saveFile(MultipartFile file, UserDataDto userDataDto);
     Optional<FileDto> findFileByStorageName(String storageName);
     List<FileDto> findAllFiles();
     File downloadFile(String storageName);
     byte[] downloadFileInBytes(File file);
     boolean checkExtension(List<MultipartFile> list);
}
