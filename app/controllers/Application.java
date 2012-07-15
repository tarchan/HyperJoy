package controllers;

import play.*;
import play.db.jpa.GenericModel.JPAQuery;
import play.mvc.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;

import models.*;

public class Application extends Controller
{
	static final String HYPERJOY = "http://homepage1.nifty.com/yottoide/hyperjoy.html";

//	@Before
//	public static void loadSongs()
//	{
////		List<Song> songs = Song.findAll();
////		if (songs.size() == 0)
//		if (Song.count() == 0)
//		{
//			Song.deleteAll();
////			Song.loadAll(HYPERJOY);
//			Bootstrap.loadAll(HYPERJOY);
//		}
//	}

	public static void index0(String keyword)
	{
		render(keyword);
	}

	static void upgradeSongs(List<Song> songs)
	{
		if (songs == null) return;

		for (Song song : songs)
		{
			song.upgrade();
		}
	}

	public static void index(String keyword)
	{
		List<Song> songs = null;
		long total = 0;
		try
		{
			total = Song.count();
//		List<Song> songs = Song.findAll();
//		int total = songs.size();
			if (keyword != null && keyword.length() > 0)
			{
//			List<Song> songs = Song.find("keywords like ?", "%" + keyword + "%").fetch();
//			List<Song> songs = Song.find("byKeywordsIlike", "%" + keyword + "%").fetch();
				songs = Song.byKeywords(keyword);
				upgradeSongs(songs);
//			if (songs != null)
//			{
//				for (Song song : songs)
//				{
//					song.upgrade();
//				}
//			}
//				render(keyword, songs, total);
			}
//			else
//			{
//				render(keyword, total);
//			}
		}
		finally
		{
			render(keyword, songs, total);
		}
	}

	public static void anime(String keyword)
	{
////		List<Song> songs = Song.find("isVocaloid is false and isToho is false and keywords like ?", "%" + keyword + "%").fetch();
////		List<Song> songs = Song.find("byKeywordsIlikeAndIsVocaloidAndIsToho", "%" + keyword + "%", false, false).fetch();
//		List<Song> songs = Song.byKeywords(keyword, false, false);
//		upgradeSongs(songs);
////		for (Song song : songs)
////		{
////			song.upgrade();
////		}
//		long total = Song.count("isVocaloid is false and isToho is false");
//		render("@index", keyword, songs, total);
		List<Song> songs = null;
		long total = 0;
		try
		{
			songs = Song.byKeywords(keyword, false, false);
			upgradeSongs(songs);
			total = Song.count("isVocaloid is false and isToho is false");
			render("@index", keyword, songs, total);
		}
		finally
		{
			render("@index", keyword, songs, total);
		}
	}

	public static void vocaloid(String keyword)
	{
////		List<Song> songs = Song.find("isVocaloid is true and keywords like ?", "%" + keyword + "%").fetch();
////		List<Song> songs = Song.find("byKeywordsIlikeAndType", "%" + keyword + "%", "vocaloid").fetch();
//		List<Song> songs = Song.byKeywords(keyword, true, false);
//		upgradeSongs(songs);
////		for (Song song : songs)
////		{
////			song.upgrade();
////		}
//		long total = Song.count("isVocaloid is true");
//		render("@index", keyword, songs, total);
		List<Song> songs = null;
		long total = 0;
		try
		{
			songs = Song.byKeywords(keyword, true, false);
			upgradeSongs(songs);
			total = Song.count("isVocaloid is true");
			render("@index", keyword, songs, total);
		}
		finally
		{
			render("@index", keyword, songs, total);
		}
	}

	public static void toho(String keyword)
	{
////		List<Song> songs = Song.find("isToho is true and keywords Like ?", "%" + keyword + "%").fetch();
////		List<Song> songs = Song.find("byKeywordsIlikeAndType", "%" + keyword + "%", "toho").fetch();
//		List<Song> songs = Song.byKeywords(keyword, false, true);
//		upgradeSongs(songs);
////		for (Song song : songs)
////		{
////			song.upgrade();
////		}
//		long total = Song.count("isToho is true");
//		render("@index", keyword, songs, total);
		List<Song> songs = null;
		long total = 0;
		try
		{
			songs = Song.byKeywords(keyword, false, true);
			upgradeSongs(songs);
			total = Song.count("isToho is true");
			render("@index", keyword, songs, total);
		}
		finally
		{
			render("@index", keyword, songs, total);
		}
	}

	public static void all(String keyword)
	{
		List<Song> songs = null;
		long total = 0;
		try
		{
			songs = Song.byKeywords(keyword);
			upgradeSongs(songs);
			total = Song.count();
			render("@index", keyword, songs, total);
		}
		finally
		{
			render("@index", keyword, songs, total);
		}
	}

	public static void findSong(String tag, String keyword)
	{
		if (tag.equals("anime"))
		{
			anime(keyword);
		}
		else if (tag.equals("vocaloid"))
		{
			vocaloid(keyword);
		}
		else if (tag.equals("toho"))
		{
			toho(keyword);
		}
		else
		{
			index(keyword);
		}
	}
}
