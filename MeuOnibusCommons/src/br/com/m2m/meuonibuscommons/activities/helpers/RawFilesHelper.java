package br.com.m2m.meuonibuscommons.activities.helpers;import java.io.ByteArrayOutputStream;import java.io.IOException;import java.io.InputStream;import android.content.Context;public class RawFilesHelper {		public static String loadRawFileWith(Context context, int resourceId) {		InputStream rawResource = context.getResources().openRawResource(				resourceId);		try {			ByteArrayOutputStream baos = new ByteArrayOutputStream();			byte[] buffer = new byte[1024];			int length = 0;			while ((length = rawResource.read(buffer)) != -1) {				baos.write(buffer, 0, length);			}			return new String(baos.toByteArray());		} catch (IOException e) {			return "";		}	}}