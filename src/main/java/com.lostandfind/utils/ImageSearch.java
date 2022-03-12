package com.lostandfind.utils;

import com.aliyun.imagesearch20201214.Client;
import com.aliyun.imagesearch20201214.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class ImageSearch {
    public static String accessKeyId = "LTAI5tRsRivnJphWGW7KBsd3";
    public static String accessKeySecret = "rg5bB00rWnzKRvaOQPNNSWBbjIHHFL";
    public static String type = "access_key";
    public static String endpoint = "imagesearch.cn-hangzhou.aliyuncs.com";
    public static String regionId = "cn-hangzhou";
    public static String instanceName = "lostandfind";
    public static Client client = null;

    public ImageSearch(){
        Config authConfig = new Config();
        authConfig.accessKeyId = accessKeyId;
        authConfig.accessKeySecret = accessKeySecret;
        authConfig.type = type;
        authConfig.endpoint = endpoint;
        authConfig.regionId = regionId;
        try {
            client = new Client(authConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public AddImageResponse Add(String productId, String picName, String customContent, InputStream inputStream) throws Exception {
        AddImageAdvanceRequest request = new AddImageAdvanceRequest();
        // 必填，图像搜索实例名称。
        request.instanceName = instanceName;

        // 必填，商品id，最多支持 512个字符。
        // 一个商品可有多张图片。
        request.productId = productId;

        // 必填，图片名称，最多支持 512个字符。
        // 1. ProductId + PicName唯一确定一张图片。
        // 2. 如果多次添加图片具有相同的ProductId + PicName，以最后一次添加为准，前面添加的图片将被覆盖。
        request.picName = picName;

        // 选填，用户自定义的内容，最多支持4096个字符。
        // 查询时会返回该字段。例如可添加图片的描述等文本。
        request.customContent = customContent;

        // 选填，是否需要进行主体识别，默认为true。
        // 1.为true时，由系统进行主体识别，以识别的主体进行搜索，主体识别结果可在Response中获取。
        // 2. 为false时，则不进行主体识别，以整张图进行搜索。
        // 3.对于布料图片搜索，此参数会被忽略，系统会以整张图进行搜索。
        request.crop = true;

        RuntimeOptions runtimeOptions = new RuntimeOptions();

        // 图片内容，最多支持 4MB大小图片以及5s的传输等待时间。当前仅支持PNG、JPG、JPEG、BMP、GIF、WEBP、TIFF、PPM格式图片；
        // 对于商品、商标、通用图片搜索，图片长和宽的像素必须都大于等于100且小于等于4096；
        // 对于布料搜索，图片长和宽的像素必须都大于等于448且小于等于4096；
        // 图像中不能带有旋转信息
        request.picContentObject = inputStream;
        AddImageResponse response = client.addImageAdvance(request,runtimeOptions);
        return response;
//        try {
//            AddImageResponse response = client.addImageAdvance(request,runtimeOptions);
//            System.out.println("success: " + response.getBody().success + ". message: "
//                    + response.getBody().message        + ". categoryId: "
//                    + response.getBody().picInfo.categoryId + ". region:"
//                    + response.getBody().picInfo.region
//                    + ". requestId: " + response.getBody().requestId);
//        } catch (TeaException e) {
//            System.out.println(e.getCode());
//            System.out.println(e.getData());
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
    }

    public DeleteImageResponse Delete() throws Exception{
        DeleteImageRequest request = new DeleteImageRequest();
        // 必填，图像搜索实例名称。
        request.instanceName = instanceName;
        // 必填，商品id，最多支持 512个字符。
        // 一个商品可有多张图片。
        request.productId = "test";
        // 选填，图片名称。若不指定本参数，则删除ProductId下所有图片；若指定本参数，则删除ProductId+PicName指定的图片。
        request.picName = "1000";

        DeleteImageResponse response = client.deleteImage(request);
        // System.out.println("requestId: " + response.requestId + ". success: " + response.success + ". message: " + response.message);

        return response;
    }

    public SearchImageByPicResponse Search(InputStream inputStream) throws Exception{
        SearchImageByPicAdvanceRequest request = new SearchImageByPicAdvanceRequest();
        // 必填，图像搜索实例名称。
        request.instanceName = instanceName;

        // 选填，返回结果的数目。取值范围：1-100。默认值：10。
        request.num = 10;

        // 选填，返回结果的起始位置。取值范围：0-499。默认值：0。
        request.start = 0;

        // 选填，是否需要进行主体识别，默认为true。
        // 1.为true时，由系统进行主体识别，以识别的主体进行搜索，主体识别结果可在Response中获取。
        // 2. 为false时，则不进行主体识别，以整张图进行搜索。
        // 3.对于布料图片搜索，此参数会被忽略，系统会以整张图进行搜索。
        request.crop = true;

        request.picContentObject = inputStream;

        RuntimeOptions runtimeObject =  new RuntimeOptions();
        SearchImageByPicResponse response = client.searchImageByPicAdvance(request, runtimeObject);
        return response;
//        try {
//            SearchImageByPicResponse response = client.searchImageByPicAdvance(request, runtimeObject);
//            List<SearchImageByPicResponseBody.SearchImageByPicResponseBodyAuctions> auctions = response.getBody().getAuctions();
//            for(SearchImageByPicResponseBody.SearchImageByPicResponseBodyAuctions auction:auctions) {
//                System.out.println(auction.categoryId + " " + auction.picName + " "
//                        + auction.productId + " " + auction.customContent + " " + auction.score + " "
//                        + auction.strAttr + " " + auction.intAttr);
//            }
//            System.out.println("多主体信息");
//            SearchImageByPicResponseBody.SearchImageByPicResponseBodyPicInfo picInfo = response.getBody().getPicInfo();
//            for (SearchImageByPicResponseBody.SearchImageByPicResponseBodyPicInfoMultiRegion multiRegion : picInfo.getMultiRegion()) {
//                System.out.println(multiRegion.region);
//            }
//        } catch (TeaException e) {
//            System.out.println(e.getCode());
//            System.out.println(e.getData());
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
    }
}
