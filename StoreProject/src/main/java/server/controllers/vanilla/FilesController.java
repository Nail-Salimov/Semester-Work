package server.controllers.vanilla;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.bl.services.file.FileService;
import server.entities.file.dto.FileDto;
import server.entities.user.model.UserDataModel;
import server.security.details.UserDetailImpl;

import java.io.File;
import java.util.Optional;

@Controller
public class FilesController {

    @Autowired
    private FileService fileService;


    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/files", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, Authentication authentication) {

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        assert extension != null;
        if (extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg")) {
            FileDto fileDto = fileService.saveFile(file, userDataModel);
            return ResponseEntity.ok().body(fileDto.getStorageName());
        }

        return ResponseEntity.ok().body("is not jpg/png/jpeg");
    }


    @GetMapping(produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @RequestMapping(value = "/image/{image-name:.+}", method = RequestMethod.GET)
    public @ResponseBody byte[] getFile(@PathVariable("image-name") String imageName) {

        Optional<FileDto> optionalFileDto = fileService.findFileByStorageName(imageName);

        if (optionalFileDto.isPresent()) {
            File file = fileService.downloadFile(optionalFileDto.get().getStorageName());
            return fileService.downloadFileInBytes(file);
        } else {
            return null;
        }
    }
}