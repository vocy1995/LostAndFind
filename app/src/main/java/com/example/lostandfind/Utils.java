package com.example.lostandfind;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os) //이미지 사용을 위한 클래스
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}
