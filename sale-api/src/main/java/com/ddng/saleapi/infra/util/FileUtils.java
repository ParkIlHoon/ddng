package com.ddng.saleapi.infra.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class FileUtils
{
    private static final String BASE_PATH = "/Users/1hoon/workspace/file_ddng";

    public String saveBase64AsFile (String uploadPath, String base64String)
    {
        String result = "";
        byte[] decode = Base64.decodeBase64(base64String);
        String fileUploadPath = BASE_PATH + File.separator  + StringUtils.cleanPath(uploadPath);
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
        String path = BASE_PATH + File.separator + StringUtils.cleanPath(filePath);

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
