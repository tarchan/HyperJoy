package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller
{
	static final String HYPERJOY = "http://homepage1.nifty.com/yottoide/hyperjoy.html";

	@Before
	public static void loadSongs()
	{
		List<Song> songs = Song.findAll();
//		if (songs.size() == 0)
		{
			Song.deleteAll();
			Song.load(HYPERJOY);
		}
	}

	public static void index(String keyword)
	{
		List<Song> songs = Song.findAll();
		if (keyword != null && keyword.length() > 0)
		{
			songs = Song.find("keywords like ?", "%" + keyword + "%").fetch();
		}
		render(keyword, songs);
	}
}
