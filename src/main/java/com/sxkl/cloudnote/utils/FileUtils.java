package com.sxkl.cloudnote.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.image.entity.Image;
import com.sxkl.cloudnote.image.service.ImageService;

import lombok.Cleanup;

public class FileUtils extends org.apache.commons.io.FileUtils {
	public static void writeFileToDisk(byte[] data, String filePath) throws Exception {
		FileOutputStream fos = null;
		if ((StringUtils.isNotEmpty(filePath)) && (data != null)) {
			fos = new FileOutputStream(filePath);
			fos.write(data);
			fos.close();
		}
	}

	public static byte[] readFileToByteArray(String filePath) throws Exception {
		File file = new File(filePath);
		InputStream in = new FileInputStream(file);
		try {
			return IOUtils.toByteArray(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public static String getUUIDName(String fileName) {
		String[] split = fileName.split("\\.");
		String extendFile = "." + split[(split.length - 1)].toLowerCase();
		return UUID.randomUUID().toString() + extendFile;
	}
	
	public static String getUUIDNameWithoutextend(String fileName) {
		return UUID.randomUUID().toString();
	}

	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	public static String getFileName(String fileName) {
		String[] split = fileName.split("\\.");
		return split[0];
	}

	public static String getUrlFileName(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}

	public static String getFileTypeByName(String fileName) {
		String[] split = fileName.split("\\.");
		String extendFile = split[(split.length - 1)].toLowerCase();
		return extendFile;
	}

	public static void createFileWithEncoder(String filePath, String content, String encoder) throws Exception {
		FileOutputStream fos = new FileOutputStream(filePath, false);
		OutputStreamWriter osw = new OutputStreamWriter(fos, encoder);
		osw.write(content);

		osw.close();
		fos.close();
	}

	public static void createDirs(String dirs) throws Exception {
		File file = new File(dirs);
		file.mkdirs();
	}

	public static String save(File f, String path) throws Exception {
		File dest = new File(path);
		if (!dest.getParentFile().exists())
			dest.getParentFile().mkdirs();
		copyFile(f, dest);
		return path;
	}

	public static void delete(String path) throws Exception {
		File dest = new File(path);
		if (dest.exists())
			forceDelete(dest);
	}

	public static void delete(File dest) throws Exception {
		if (dest.exists())
			forceDelete(dest);
	}

	public static void saveFileByUrl(String url, String filePath) throws Exception {
		URL u = new URL(url);

		HttpURLConnection httpUrl = (HttpURLConnection) u.openConnection();
		//防止403
		httpUrl.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		BufferedInputStream bis = null;

		FileOutputStream fos = null;
		try {
			bis = new BufferedInputStream(httpUrl.getInputStream());
			delete(filePath);
			fos = new FileOutputStream(new File(filePath), true);
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				fos.write(buff, 0, bytesRead);
			}
		} catch(Exception e){
			e.printStackTrace();
		}finally {
			if (bis != null)
				bis.close();
			if (fos != null)
				fos.close();
		}
	}
	
	public static void saveFileToDBByUrl(String url, Image image, ImageService imageService) throws Exception {
		URL u = new URL(url);
		HttpURLConnection httpUrl = (HttpURLConnection) u.openConnection();
		//防止403
		httpUrl.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		httpUrl.setRequestMethod("GET");
		InputStream inStream = httpUrl.getInputStream();
		byte[] data = readInputStream(inStream);
		image.setContent(data);
		imageService.saveImage(image);
	}
	
	public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        //创建一个Buffer字符串  
        byte[] buffer = new byte[1024];  
        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
        int len = 0;  
        //使用一个输入流从buffer里把数据读取出来  
        while( (len=inStream.read(buffer)) != -1 ){  
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
            outStream.write(buffer, 0, len);  
        }  
        //关闭输入流  
        inStream.close();  
        //把outStream里的数据写入内存  
        return outStream.toByteArray();  
    }  

	public static String saveHtmlImgToLocal(String domain, String html, String savePath, String userId) {
		Document doc = Jsoup.parse(html);

		Elements imgs = doc.getElementsByTag("img");

		String ymd = DateUtils.formatDate2Str("yyyyMMdd");
		savePath = savePath + "/" + ymd;
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		String imgName = "";
		String imgNewUrl = "";
		String regex = "^(http|https|ftp)+://.*$";
		for (int i = 0; i < imgs.size(); i++) {
			String imgOldUrl = imgs.get(i).attr("src");
			if ((Pattern.matches(regex, imgOldUrl)) && (!imgOldUrl.startsWith(domain))) {
				imgName = getUUIDName(getUrlFileName(imgOldUrl));
				try {
					saveFileByUrl(imgOldUrl, savePath + "/" + imgName);
					imgNewUrl = domain 
							+ MessageUtils.setParamMessage("/websrc/file/{0}/picture", new String[] { userId }) + "/"
							+ ymd + "/" + imgName;
					doc.getElementsByTag("img").get(i).attr("src", imgNewUrl);
				} catch (Exception localException) {
				}
			}
		}
		return doc.html();
	}
	
	public static String saveHtmlImgToDB(String html, ImageService imageService) {
		Document doc = Jsoup.parse(html);
		Elements imgs = doc.getElementsByTag("img");
		String imgName = "";
		String imgNewUrl = "";
		String regex = "^(http|https|ftp)+://.*$";
		for (int i = 0; i < imgs.size(); i++) {
			String imgOldUrl = imgs.get(i).attr("src");
			String imgAlt = imgs.get(i).attr("alt");
			if ((Pattern.matches(regex, imgOldUrl)) && (!imgOldUrl.startsWith(Constant.ARTICLE_CONTENT_DOMAIN))) {
				imgName = getUUIDNameWithoutextend(getUrlFileName(imgOldUrl));
				try {
					Image image = new Image(imgName,imgAlt);
					saveFileToDBByUrl(imgOldUrl,image,imageService);
					imgNewUrl = Constant.ARTICLE_CONTENT_DOMAIN + "/image/getImage?name="+imgName;
					doc.getElementsByTag("img").get(i).attr("src", imgNewUrl);
				} catch (Exception localException) {
				}
			}
		}
		return doc.html();
	}
}