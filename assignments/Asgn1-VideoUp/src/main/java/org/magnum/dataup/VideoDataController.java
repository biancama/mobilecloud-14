package org.magnum.dataup;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by massimo on 14/09/14.
 */
@Controller
public class VideoDataController {
	@RequestMapping(value="/video/{id}/data", method= RequestMethod.POST)
	public @ResponseBody VideoStatus setVideoData(@PathVariable("id") long id,
	                                              @RequestParam(value = "data") MultipartFile file){
		if (!file.isEmpty()) {

				Video v = new Video();
				v.setId(id);
				byte[] bytes = new byte[0];

			try {
				if (VideoFileManager.get().hasVideoData(v)) {

					bytes = file.getBytes();
					ByteArrayInputStream fileInputStream = new ByteArrayInputStream(bytes);
					VideoFileManager.get().saveVideoData(v, fileInputStream);
				} else {
					throw new ResourceNotFoundException();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			return new VideoStatus(VideoStatus.VideoState.READY);
		} else {
			return new VideoStatus(VideoStatus.VideoState.PROCESSING);
		}
	}
	@RequestMapping(value="/video/{id}/data", method= RequestMethod.GET)
	public void getData(@PathVariable("id") long id, HttpServletResponse response){
		Video video = new Video();
		video.setId(id);

		try {
			if (VideoFileManager.get().hasVideoData(video)){
				VideoFileManager.get().copyVideoData(video, response.getOutputStream());
				response.flushBuffer();
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
