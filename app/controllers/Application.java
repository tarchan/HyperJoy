package controllers;

import play.*;
import play.mvc.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import models.*;

public class Application extends Controller
{
	static final String HYPERJOY = "http://homepage1.nifty.com/yottoide/hyperjoy.html";

	@Before
	public static void loadSongs()
	{
//		List<Song> songs = Song.findAll();
		if (Song.count() == 0)
		{
			Song.deleteAll();
			Song.load(HYPERJOY);
		}
	}

	public static void index(String keyword)
	{
//		try
//		{
//			String q = request.querystring;
//			q = URLDecoder.decode(q, "utf-8");
//			Logger.info("querystring=%s", q);
//		}
//		catch (UnsupportedEncodingException x)
//		{
//		}

		long total = Song.count();
		List<Song> songs;
		if (keyword != null && keyword.length() > 0)
		{
			songs = Song.find("keywords like ?", "%" + keyword + "%").fetch();
			for (Song song : songs)
			{
				song.upgrade();
			}
		}
		else
		{
			songs = null;
		}
		render(keyword, songs, total);
	}
}
