package cn.xinill.ttms.controller;

import cn.xinill.ttms.common.ServerResponse;
import cn.xinill.ttms.pojo.VOIntegerId;
import cn.xinill.ttms.pojo.VOMovie;
import cn.xinill.ttms.pojo.VOMovieList;
import cn.xinill.ttms.service.IMovieService;
import cn.xinill.ttms.utils.ImgException;
import cn.xinill.ttms.utils.OSSClientUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Xinil
 * @Date: 2021/4/8 21:07
 */
@Controller
@RequestMapping(value = "/movie")
public class MovieController {

    private IMovieService movieService;
    private OSSClientUtil ossClientUtil;
    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Autowired
    public void setMovieService(IMovieService movieService) {
        this.movieService = movieService;
    }

    @Autowired
    public void setOssClientUtil(OSSClientUtil ossClientUtil) {
        this.ossClientUtil = ossClientUtil;
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/upload-cover")
    public ServerResponse<String> insertMovie(@RequestParam MultipartFile cover){
        try {
            logger.info("[上传图片]");
            String urlFileName = ossClientUtil.uploadImg2Oss(cover);
            logger.info("[上传图片]：上传成功，文件名:"+urlFileName);
            return ServerResponse.createBySuccessMsgData("上传图片成功", urlFileName);
        } catch (ImgException e) {
            logger.error("[上传图片]：/movie/upload-cover 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/insert")
    public ServerResponse<Boolean> insertMovie(@RequestBody VOMovie voMovie){
        try {
            logger.info("[添加剧目信息]："+voMovie);
            if(movieService.insertMovie(voMovie) == 1){
                logger.info("[添加剧目信息]：成功添加剧目信息");
                return ServerResponse.createBySuccessMsgData("成功添加",true);
            }else{
                logger.warn("[添加剧目信息]：添加剧目信息失败");
                return ServerResponse.createByErrorMsgData("添加失败", false);
            }
        } catch (Exception e) {
            logger.error("[添加剧目信息]：/movie/insert 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器繁忙", false);
        }
    }



    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/getList")
    public ServerResponse<VOMovieList> getMovieList(@RequestParam String sortType,
                                                    @RequestParam String sortRule,
                                                    @RequestParam int page,
                                                    @RequestParam int pageLimit){

        try {
            logger.info("[获取剧目信息列表]：sortType="+sortType+"  sortRole="+sortRule+"  page="+page+"    pageLimit="+pageLimit);
            if(!sortType.equals("title") && !sortType.equals("releaseDate") && !sortType.equals("rate")){
                logger.warn("[获取剧目信息列表]：排序参数不合法");
                return ServerResponse.createByErrorMsg("排序参数不合法");
            }

            if(sortRule.equals("down")){
                sortRule = "DESC";
            }else{
                sortRule = "ASC";
            }

            int start = (page-1)*pageLimit;
            logger.info("[获取剧目信息列表]： 查询信息成功");
            VOMovieList movieList = movieService.getMovieList(sortType, sortRule, start, pageLimit);
            return ServerResponse.createBySuccessMsgData("查询电影成功",movieList);
        } catch (Exception e) {
            logger.error("[获取剧目信息列表]： /movie/getList 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器繁忙", null);
        }
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/getMovie")
    public ServerResponse<VOMovie> getMovie(@RequestBody Integer id){
        try{
            logger.info("[查询单个剧目信息]： 剧目id:"+id);
            VOMovie voMovie = movieService.getMovie(id);
            logger.info("[查询单个剧目信息]： 查询成功："+voMovie);
            return ServerResponse.createBySuccessMsgData("查询电影成功", voMovie);
        }catch (Exception e) {
            logger.error("[查询单个剧目信息]： /movie/getMovie 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器繁忙", null);
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    public ServerResponse<Boolean> deleteMovie(@RequestBody VOIntegerId voInteger){
        try {
            logger.info("[删除剧目信息]： 剧目id:"+voInteger.getId());
            if(movieService.deleteMovie(voInteger.getId())){
                logger.info("[删除剧目信息]： 删除剧目信息成功");
                return ServerResponse.createBySuccessMsgData("剧目删除成功", true);
            }
            return ServerResponse.createBySuccessMsgData("剧目不存在", true);
        } catch (Exception e) {
            logger.info("[删除剧目信息]： /movie/delete 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器繁忙", false);
        }
    }
}
