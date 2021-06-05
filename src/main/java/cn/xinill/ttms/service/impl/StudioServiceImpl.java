package cn.xinill.ttms.service.impl;

import cn.xinill.ttms.mapper.ISeatMapper;
import cn.xinill.ttms.mapper.IStudioMapper;
import cn.xinill.ttms.po.Seat;
import cn.xinill.ttms.po.Studio;
import cn.xinill.ttms.service.IStudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/5/8 20:01
 */
@Service
public class StudioServiceImpl implements IStudioService {

    private IStudioMapper studioMapper;
    private ISeatMapper seatMapper;

    @Autowired(required = false)
    public void setStudioMapper(IStudioMapper studioMapper) {
        this.studioMapper = studioMapper;
    }

    @Autowired(required = false)
    public void setSeatMapper(ISeatMapper seatMapper) {
        this.seatMapper = seatMapper;
    }

    @Override
    public boolean insert(Studio studio) {
        System.out.println("[新增演出厅信息]："+studio);
        studioMapper.insert(studio.getName(), studio.getRow(), studio.getCol());

        int studioId = studioMapper.findIdByName(studio.getName());
        addStudioSeat(studioId, studio.getRow(), studio.getCol());
        return true;
    }

    @Override
    public boolean modifyStudio(Studio studio) {
        studioMapper.modifyStudio(studio);
        int row = studio.getRow(), col = studio.getCol();

        int[][] seats = new int[row+1][col+1];
        for(int[] s: seats){
            Arrays.fill(s, 1);
        }
        List<Seat> list = findSeatListByStudioId(studio.getId());
        for(Seat seat: list){
            if(seat.getRow() <= row && seat.getCol() <= col){
                seats[seat.getRow()][seat.getCol()] = seat.getStatus();
            }
        }
        seatMapper.deleteByStudioId(studio.getId());
        for(int i=1; i<=row; ++i){
            for(int j=1; j<=col; ++j){
                seatMapper.insert(studio.getId(), i, j, seats[i][j]);
            }
        }
        return true;
    }

    @Override
    public boolean deleteStudio(int studioId) {
        studioMapper.deleteByStudioId(studioId);
        seatMapper.deleteByStudioId(studioId);
        return true;
    }

    @Override
    public List<Studio> findAllStudio() {
        return studioMapper.findAllStudio();
    }

    public List<Seat> findSeatListByStudioId(int studioId){
        return seatMapper.findSeatListByStudioId(studioId);
    }


    public boolean addStudioSeat(Integer studioId, int row, int col){
        for(int i=1; i<=row; ++i){
            for(int j=1; j<=col; ++j){
                if(1 != seatMapper.insert(studioId, i, j, 1)){
                    System.out.println("[影厅id:"+studioId+"]: 第"+i+"行第"+j+"列添加座位失败");
                }
            }
        }
        return true;
    }

    public boolean updateSeatStatus(Seat seat){
        return 1 == seatMapper.updateStatus(seat);
    }

    @Override
    public Studio findStudioById(int studioId) {
        return studioMapper.findStudio(studioId);
    }
}
