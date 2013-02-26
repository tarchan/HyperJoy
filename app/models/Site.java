package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;

import org.hibernate.annotations.Index;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

@Entity
public class Site extends Model
{
	/** サイトID */
	@Index(name = "name")
	public String name;

	/** サイトURL */
	@Lob
	public String url;

	/** 最終更新日 */
	public long lastModified;

	/** 読み込み中は true */
	public boolean loading;

	public Site(String name, String url)
	{
		this.name = name;
		this.url = url;
	}

	public void beginLoading()
	{
		try
		{
			URLConnection con = new URL(url).openConnection();
			long now = con.getLastModified();
			lastModified = now;
			Logger.info("最終更新日=%s", new Date(lastModified));

			loading = true;
			save();
		}
		catch (IOException x)
		{
			Logger.error(x, "サイトにアクセスできません。");
		}
	}

	public void endLoading()
	{
		loading = false;
		save();
	}
}
