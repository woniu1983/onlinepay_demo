package cn.woniu.onlinepay.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesHelper {
	Properties properties = new Properties();
	
	private void load(InputStream stream)
	{
		if(stream == null)
		{
			return;
		}
		properties.clear();
		try {
			properties.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	public void load(String file)
	{
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
			load(stream);
		} catch (FileNotFoundException e) {
			e.getMessage();
			return;
		} finally {
			if(stream != null)
			{
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	public void fromByteArray(byte[] input)
	{
		ByteArrayInputStream stream = new ByteArrayInputStream(input);
		load(stream);
		if(stream != null)
		{
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void save(OutputStream stream)
	{
		if(stream == null)
		{
			return;
		}
		try {
			properties.store(stream, "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void save(String file)
	{
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(file);
			save(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(stream != null)
			{
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public byte[] toByteArray()
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		save(stream);
		byte[] output = stream.toByteArray();
		
		if(stream != null)
		{
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output;
	}
	public void setPropery(String key, String value)
	{
		properties.setProperty(key, value);
	}
	public String getProperty(String key, String default_value)
	{
		String value = properties.getProperty(key);
		if(value == null)
		{
			// not found property
			value = default_value;
		}
		return value;
	}
	public void clear()
	{
		properties.clear();
	}
	public static void main(String[] args)
	{
		PropertiesHelper helper = new PropertiesHelper();
		helper.setPropery("a", "1");
		helper.setPropery("b", "2");
		helper.save("d:\\abc.txt");
		helper.clear();
		helper.load("d:\\abc.txt");
		byte[] data = helper.toByteArray();
		
		helper.clear();
		helper.fromByteArray(data);
	}
}
