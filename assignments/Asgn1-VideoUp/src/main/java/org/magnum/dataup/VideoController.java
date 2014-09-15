package org.magnum.dataup;

import org.magnum.dataup.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * Created by massimo on 26/08/14.
 */
@Controller
@RequestMapping("/video")
public class VideoController {

	@RequestMapping(method = RequestMethod.GET)
	public  @ResponseBody Collection<Video> getVideos(){
		Collection<Video> videos = null;

		try {
			videos = VideoFileManager.get().findAllVideos();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return videos;
	}
	@RequestMapping(method = RequestMethod.POST)
	public  @ResponseBody Video addVideo(@RequestBody Video video){
		try {
			VideoFileManager.get().saveVideo(video);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return video;
	}


}
