package com.ddng.customerapi.infra.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class FileUtils
{
    @Value("${service.file:/Users/1hoon/workspace/file_ddng/}")
    private String filePath;

    public String saveBase64AsFile (String uploadPath, String base64String)
    {
        String result = "";
        byte[] decode = Base64.decodeBase64(base64String);
        String fileUploadPath = filePath  + StringUtils.cleanPath(uploadPath);
        FileOutputStream fos = null;

        try
        {
            fos = new FileOutputStream(fileUploadPath);
            fos.write(decode);
            result = fileUploadPath;
        }
        catch (FileNotFoundException e)
        {
            result = "";
            e.printStackTrace();
        } catch (IOException e)
        {
            result = "";
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e)
            {
                result = "";
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean deleteFile (String filePath)
    {
        String path = filePath + StringUtils.cleanPath(filePath);

        File file = new File(path);

        if (file.exists())
        {
            return file.delete();
        }
        else
        {
            return false;
        }
    }
}
