/**
 * Copyright 2014-2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.webase.front.solc;

import com.webank.webase.front.base.code.ConstantCode;
import com.webank.webase.front.base.exception.FrontException;
import com.webank.webase.front.base.response.BaseResponse;
import com.webank.webase.front.solc.entity.RspDownload;
import com.webank.webase.front.solc.entity.SolcInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * upload and download solc js file
 */
@Slf4j
@RestController
@RequestMapping("solc")
public class SolcController {

	@Autowired
	private SolcService solcService;

	@ApiOperation(value = "upload solc js file", notes = "upload solc js file")
	@ApiImplicitParam(name = "version", value = "solc js version tag", required = true, dataType = "String")
	@PostMapping("/upload")
	public BaseResponse upload(@RequestParam("fileName") String fileName,
							   @RequestParam("solcFile") MultipartFile solcFile,
							   @RequestParam(value = "description", required = false, defaultValue = "") String description) {
		solcService.saveSolcFile(fileName, solcFile, description);
		return new BaseResponse(ConstantCode.RET_SUCCESS);
	}

	@ApiOperation(value = "get uploaded solc file info list", notes = "list uploaded file info")
	@GetMapping("/list")
	public BaseResponse getSolcList() {
		// get list
		List<SolcInfo> resList = solcService.getAllSolcInfo();
		if (resList.isEmpty()) {
			return new BaseResponse(ConstantCode.RET_SUCCESS_EMPTY_LIST);
		}
		return new BaseResponse(ConstantCode.RET_SUCCESS, resList);
	}

	/**
	 * download Solc js file
	 */
	@ApiOperation(value = "download existed solc js", notes = "download solc js file")
	@ApiImplicitParam(name = "fileName", value = "solc file name", required = true,
			dataType = "String", paramType = "path")
	@PostMapping("/download/{fileName}")
	public ResponseEntity<InputStreamResource> downloadSolcFile(@PathVariable("fileName") String fileName) {
		if (fileName.isEmpty()) {
			throw new FrontException(ConstantCode.PARAM_FAIL_SOLC_FILE_NAME_EMPTY);
		}
		log.info("downloadSolcFile start. fileName:{}", fileName);
		RspDownload rspDownload = solcService.getSolcFile(fileName);
		return ResponseEntity.ok().headers(headers(rspDownload.getFileName()))
				.body(new InputStreamResource(rspDownload.getInputStream()));
	}

	private HttpHeaders headers(String fileName) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION,
				"attachment;filename*=UTF-8''" + encode(fileName));
		return httpHeaders;
	}

	private String encode(String name) {
		try {
			return URLEncoder.encode(name, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
