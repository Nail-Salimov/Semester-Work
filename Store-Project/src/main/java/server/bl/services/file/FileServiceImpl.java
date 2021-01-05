package server.bl.services.file;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import server.addition.loader.FileLoader;
import server.bl.repositories.file.FileRepository;
import server.entities.file.dto.FileDto;
import server.entities.file.model.FileModel;
import server.entities.user.dto.UserDataDto;
import server.entities.user.model.Role;
import server.entities.user.model.State;
import server.entities.user.model.UserDataModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileLoader fileLoader;

    @Override
    public FileDto saveFile(MultipartFile file, UserDataModel userDto) {

        FileModel fileModel = fileLoader.uploadFile(file, userDto);
        fileModel = fileRepository.save(fileModel);
        return FileDto.getFileDto(fileModel);
    }

    @Override
    public FileDto saveFile(MultipartFile file, UserDataDto userDataDto) {

        UserDataModel userModel = UserDataModel.builder()
                .id(userDataDto.getId())
                .mail(userDataDto.getMail())
                .hashPassword(userDataDto.getPassword())
                .address(userDataDto.getPassword())
                .name(userDataDto.getName())
                .role(Role.valueOf(userDataDto.getRole()))
                .state(State.valueOf(userDataDto.getState()))
                .build();

        return saveFile(file, userModel);
    }

    @Override
    public Optional<FileDto> findFileByStorageName(String storageName) {
        Optional<FileModel> optionalFile = fileRepository.findByStorageName(storageName);
        return optionalFile.map(FileDto::getFileDto);
    }

    @Override
    public List<FileDto> findAllFiles() {
        List<FileModel> list = fileRepository.findAll();
        List<FileDto> fileDtoList = new LinkedList<>();
        for (FileModel f : list) {
            fileDtoList.add(FileDto.getFileDto(f));
        }
        return fileDtoList;
    }

    @Override
    public File downloadFile(String storageName){
        return fileLoader.downloadFile(storageName);
    }

    @Override
    public byte[] downloadFileInBytes(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean checkExtension(List<MultipartFile> list) {

        boolean images = true;

        if(list.size() == 0){
            return false;
        }

        for (MultipartFile file : list) {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());


            assert extension != null : images = false;
            if (!extension.equals("jpg") && !extension.equals("png") && !extension.equals("jpeg")) {
                images = false;
            }
        }
        return images;
    }
}


