package com.ddng.utilsapi.modules.file.service;

import com.ddng.utilsapi.modules.file.dto.FileSaveResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    @Value("${application.file.upload-path:/ddng_files}")
    private String uploadRootPath;

    public static final String THUMBNAIL_FOLDER = "thumbnail";


    public FileSaveResultDto saveFile(@NotEmpty String folderName, @NotNull MultipartFile file) throws IOException {
        return this.saveFile(folderName, file, 0);
    }

    public FileSaveResultDto saveFile(@NotEmpty String folderName, @NotNull MultipartFile file, int width) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String saveFileName = uuid + FilenameUtils.EXTENSION_SEPARATOR + FilenameUtils.getExtension(file.getOriginalFilename());
        String savedFilePath = writeFile(folderName, saveFileName, file);

        if (!StringUtils.isEmpty(savedFilePath)) {
            FileSaveResultDto fileSaveResultDto = new FileSaveResultDto()
                    .setFileUrl(folderName + "/" + uuid)
                    .setFilePath(savedFilePath);
    
            if (width > 0 && isImage(file)) {
                String savedThumbnailPath = writeThumbnail(folderName, saveFileName, file, width);
                fileSaveResultDto.setThumbnailUrl(folderName + "/" + THUMBNAIL_FOLDER + "/" + uuid)
                        .setThumbnailPath(savedThumbnailPath);
            }
            return fileSaveResultDto;
        } else {
            return null;
        }
    }

    public InputStream readStream(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.newInputStream(path);
    }

    public boolean removeFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.delete(path);
        return true;
    }

    private String writeFile(String folderName, String fileName, MultipartFile file) throws IOException {
        Path path = Paths.get(this.uploadRootPath, folderName, fileName);

        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }

        try(InputStream is = file.getInputStream()) {
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
            return path.toString();
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private String writeThumbnail(String folderName, String fileName, MultipartFile file, int width) {
        try(InputStream is = file.getInputStream()) {
            BufferedImage originalBufferedImage = ImageIO.read(is);
            int originWidth = originalBufferedImage.getWidth();
            int originHeight = originalBufferedImage.getHeight();
            int height = (int) Math.round((double) width / (double) originWidth * originHeight);
            int type = (originalBufferedImage.getTransparency() == Transparency.OPAQUE)? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;

            BufferedImage newBufferedImage = new BufferedImage(width, height, type);
            Graphics2D graphics = newBufferedImage.createGraphics();

            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics.drawImage(originalBufferedImage, 0, 0, width, height, null);

            File dir = new File(this.uploadRootPath + File.separator + folderName + File.separator + THUMBNAIL_FOLDER);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File thumbnailFile = new File(dir.getPath() + File.separator + fileName);
            ImageIO.write(newBufferedImage, FilenameUtils.getExtension(file.getOriginalFilename()), thumbnailFile);

            graphics.dispose();
            return thumbnailFile.getPath();
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private boolean isImage(MultipartFile file) {
        return file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE)
                || file.getContentType().equals(MediaType.IMAGE_PNG_VALUE)
                || file.getContentType().equals(MediaType.IMAGE_GIF_VALUE);
    }
}
