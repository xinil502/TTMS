package cn.xinill.ttms.controller;

import cn.xinill.ttms.common.ServerResponse;
import cn.xinill.ttms.po.Schedule;
import cn.xinill.ttms.service.ITicketService;
import cn.xinill.ttms.utils.MyException;
import cn.xinill.ttms.vo.VOSaleTicket;
import cn.xinill.ttms.vo.VOTicket;

import cn.xinill.ttms.vo.VOTicketList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/5 17:24
 */
@Controller
@RequestMapping("/ticket")
public class TicketController {

    private ITicketService ticketService;
    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Autowired
    public void setTicketService(ITicketService ticketService) {
        this.ticketService = ticketService;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/getTicketsByScheduleId")
    public ServerResponse<VOTicketList> getTicketList(@RequestParam int scheduleId){
        try {
            logger.info("[查询影票列表]： scheduleId = " + scheduleId);
            VOTicketList ticketList = ticketService.findTicketList(scheduleId);

            StringBuilder sb = new StringBuilder("ticket: {");
            for(VOTicket ticket: ticketList.getTickets()){
                sb.append(ticket.toString());
            }
            sb.append('}');
            logger.info("[查询影票列表]：成功查询影票列表" + sb.toString());

            return ServerResponse.createBySuccessMsgData("成功查询影票列表", ticketList);
        } catch (Exception e) {
            logger.error("[查询影票列表]： /ticket/getTicketsByScheduleId 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", null);
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/submitOrder")
    public ServerResponse<Boolean> submitTicket(@RequestBody VOSaleTicket tickets){
        try {
            if(tickets.getTickets().length == 0 || tickets.getTime() == null || tickets.getPhone() == null || tickets.getPhone().length() != 11) throw new MyException("参数不合法");
            logger.info("[售票员购票]： scheduleId = {phone=" +tickets.getPhone()+ Arrays.toString(tickets.getTickets()) + tickets.getTime());
            ticketService.saleTickets(tickets);
            logger.info("[售票员购票]：成功购买影票");
            return ServerResponse.createBySuccessMsgData("成功购买影票", true);
        } catch (MyException e){
            logger.error(e.getMessage());
            return ServerResponse.createByErrorMsgData(e.getMessage(), false);
        }catch (Exception e) {
            logger.error("[售票员购票]： /ticket/submitOrder 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", false);
        }
    }
}
