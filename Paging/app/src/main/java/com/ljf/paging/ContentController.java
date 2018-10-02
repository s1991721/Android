package com.ljf.paging;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mr.lin on 2018/10/2.
 * 内容控制器
 */

public class ContentController {

    private Context mContext;
    private BookPageView mBookPageView;
    private static final int CACHE_PAGE = 50;//txt预计一行约500个汉字，共1000字节约1k。缓存50行约50k
    private static final int CACHE_PRE_PAGE = 10;
    private static final int CACHE_NEXT_PAGE = CACHE_PAGE - CACHE_PRE_PAGE;
    private static final int THRESHOLD = 3;//触发更新的阀值

    private String[] content = new String[CACHE_PAGE];

    public ContentController(Context mContext, BookPageView mBookPageView) {
        this.mContext = mContext;
        this.mBookPageView = mBookPageView;
        mBookPageView.setContentController(this);
    }

    /**
     * @param curPage 实际页数
     * @return 页内容
     */
    public String getContent(int curPage) {//起始页数为1
        if (curPage < 1) {
            return "";
        }
        int position = (curPage - 1) % CACHE_PAGE;//页数对应缓存数组中的位置
        if (position > CACHE_PAGE - THRESHOLD || position < THRESHOLD) {//翻页到阀值时，预先加载
            updateContent(curPage);
        }
        return content[position];
    }

    private void updateContent(int page) {//更新page前后内数据，
        try {
            int startPage = page - CACHE_PRE_PAGE;
            int endPage = page + CACHE_NEXT_PAGE;

            if (startPage < 1) {//防止越界
                startPage = 1;
            }
            if (startPage > endPage) {
                endPage = startPage;
            }

            content = getContent(startPage, endPage);
        } catch (Exception e) {
            e.printStackTrace();
            //todo 获取内容失败
        }

    }

    /**
     * @param startPage 起始页码
     * @param endPage   结束页码
     * @return 缓存的数据
     * @throws Exception IO或越界异常
     */
    private String[] getContent(int startPage, int endPage) throws Exception {
        if (startPage < 1 || endPage < startPage) {
            throw new RuntimeException("内容越界");
        }

        String[] result = new String[endPage - startPage + 1];

        InputStream inputStream = mContext.getResources().openRawResource(R.raw.a);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        int pageNum = 1;//当前页码，每次加载都是从txt第一行读取

        int countMax = 432;//每页最大字数，width*height/（size*size）
        int numOfRow = 18;//一行字数，width/size
        String readLine = "";//读取的行内容，或上次截剩的内容
        for (int i = startPage; i <= endPage; i++) {

            StringBuilder stringBuffer = new StringBuilder();//页内容容器
            int pageCount = 0;//当前页内的容量

            while (true) {
                if (!TextUtils.isEmpty(readLine)) {//读取的行内容，或上次截剩的内容，还没处理完，如一行内容为1000，按432容量计算的话，要处理3次

                    int stringLen = readLine.length();//剩余长度
                    if (pageCount + stringLen <= countMax) {//当前页可装下
                        stringBuffer.append(readLine).append("\n");
                        pageCount += stringLen;
                        pageCount += numOfRow;//回车则少一行文字容量
                        readLine = "";//置空，读取新内容
                    } else {
                        int count = countMax - pageCount;//当前页装不下，还能装count量
                        if (count < numOfRow) {//count量小于一行的量，留到下一页
                            break;
                        }
                        //拼接一部分，让屏幕饱满
                        stringBuffer.append(getPreContent(readLine, count)).append("\n");
                        pageCount += count;
                        pageCount += numOfRow;
                        //剩下一部分，留到下一页
                        readLine = getRestContent(readLine, count);
                        break;
                    }
                } else {
                    readLine = br.readLine();//读取新内容
                }
            }
            if (pageNum >= startPage) {//为了跳过startPage的前几页
                result[i - startPage] = stringBuffer.toString();
            }
            pageNum++;

        }
        return result;
    }

    /**
     * 返回内容的前半部分
     *
     * @param content 要取的源内容
     * @param count 返回的量
     * @return 切割后的值
     */
    private String getPreContent(String content, int count) {
        if (count > content.length()) {//总数量不足截取量
            count = content.length();
        }
        return content.substring(0, count);
    }

    /**
     * 返回内容的后半部分
     *
     * @param content 要取的源内容
     * @param start 后半部分的起始位置
     * @return 切割后的值
     */
    private String getRestContent(String content, int start) {
        if (start > content.length() - 1) {//开始位置，超过总长度
            return "";
        }
        return content.substring(start, content.length());
    }

}