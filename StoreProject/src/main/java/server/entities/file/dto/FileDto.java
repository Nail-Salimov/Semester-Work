package server.entities.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.entities.file.model.FileModel;
import server.entities.user.model.UserDataModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDto {

    private Long id;

    private String originalName;
    private String storageName;

    private UserDataModel userDataModel;
    private Long size;

    public static FileDto getFileDto(FileModel fileModel){
        return FileDto.builder()
                .id(fileModel.getId())
                .originalName(fileModel.getOriginalName())
                .storageName(fileModel.getStorageName())
                .userDataModel(fileModel.getUserDataModel())
                .size(fileModel.getSize())
                .build();
    }
}
