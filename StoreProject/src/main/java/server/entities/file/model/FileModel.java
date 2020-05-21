package server.entities.file.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.entities.user.model.UserDataModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileModel {

    private Long id;

    private String originalName;
    private String storageName;

    private UserDataModel userDataModel;
    private Long size;

    public FileModel(String originalName){
        this.originalName = originalName;
    }
}
