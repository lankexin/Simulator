package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FileUtil {
    public static void writeFile(String filePath, Map<String, List<String>> stateBuffer) throws IOException {
//        FileWriter writer = null;
//        try {
//            File file = new File(filePath);
////            if (file.exists()) {
////                file.delete();
////            }
////            file.createNewFile();
//            writer = new FileWriter(file);
//            if(stateBuffer!=null) {
//                for (String key : stateBuffer.keySet()) {
//                    writer.write(key + "\n");
//                    List<String> list = stateBuffer.get(key);
//                    for (String str : list) {
//                        writer.write(str + "\n");
//                    }
//                }
//                writer.flush();
//            }
//        } catch (IOException e) {
//            System.out.println("文件写入异常"+e);
//            throw e;
//        } finally {
//            if (null != writer) {
//                try {
//                    writer.close();
//                } catch (IOException e) {
//                    System.out.println("文件写入时流关闭异常"+e);
//                }
//            }
//        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath, true);
            if(stateBuffer!=null) {
                for (String key : stateBuffer.keySet()) {
                    fos.write(key.getBytes());
                    fos.write("\r\n".getBytes());
                    List<String> list = stateBuffer.get(key);
                    for (String str : list) {
                        fos.write(str.getBytes());
                        fos.write("\r\n".getBytes());
                    }
                    fos.write("\r\n".getBytes());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fos != null){
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
