package handling.channel.handler;

import java.util.ArrayList;
import java.util.List;
import client.MapleCharacter;
import client.MapleClient;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class BeanGame {

    public static void BeanGame1(SeekableLittleEndianAccessor slea, MapleClient c) {
        //System.out.println("��???��??????" +slea.toString());
        MapleCharacter chr = c.getPlayer();
        List<Beans> beansInfo = new ArrayList<Beans>();
        
        int type = slea.readByte();
        int ���� = 0;
        int ������̖ = 0;
        if (type == 1) { //???��ʼ???ʱ??? ȷ��???????????????��
            //01 E8 03
            ���� = slea.readShort();
            chr.setBeansRange(����);
            //System.out.println("????????????��1??"+???��);
            c.getSession().write(MaplePacketCreator.enableActions());
        } else if (type == 0) { //û???��?????????
            ���� = slea.readShort();
            ������̖ = slea.readInt() + 1;
            chr.setBeansRange(����);
            chr.setBeansNum(������̖);
                if (������̖ == 1) {
                    chr.setCanSetBeansNum(false);
                }
        } else if (type == 2) { //û???��?????????
            if ((type == 11) || (type == 0)) {
                ���� = slea.readShort();
                ������̖ = slea.readInt() + 1;
                chr.setBeansRange(����);
                chr.setBeansNum(������̖);
                if (������̖ == 1) {
                    chr.setCanSetBeansNum(false);
                }
            }
        } else if (type == 6) {
                slea.skip(1);
                int ѭ�h�Δ� = slea.readByte();
                if (ѭ�h�Δ� == 0) {
                    return;
                }
                if (ѭ�h�Δ� != 1) {
                    slea.skip((ѭ�h�Δ� - 1) * 8);
                }
                if (chr.isCanSetBeansNum()) {
                    chr.setBeansNum(chr.getBeansNum() + ѭ�h�Δ�);
                }
                chr.gainBeans(-ѭ�h�Δ�);
                chr.setCanSetBeansNum(true);
        } else if ((type == 11) || (type == 6)) {
            ���� = slea.readShort();
            chr.setBeansRange(����);
            byte size = (byte) (slea.readByte()+1);
            short Pos = slea.readShort();
            byte Type = (byte) (slea.readByte() + 1);
           // for (int i = 0; i < 5; i++) {
                // beansInfo.add(new Beans(chr.getBeansRange() + rand(-100, 100), getBeanType(), chr.getBeansNum() + i));
           // }
            c.getSession().write(MaplePacketCreator.showBeans(����,size, Pos, Type));
        } else {
                System.out.println("δ̎�����͡�" + type + "��\n��" + slea.toString());
        }
    }

    private static int getBeanType() {
        int random = rand(1, 100);
        int beanType = 0;
        //3 - ??, 2 - ???, 1 - ??, 0 - ??????
        switch (random) {
            case 2:
                beanType = 1;
                break;
            case 49:
                beanType = 2;
                break;
            case 99:
                beanType = 3;
                break;
        }
        return beanType;
    }

    private static int rand(int lbound, int ubound) {
        return (int) ((Math.random() * (ubound - lbound + 1)) + lbound);
    }

    public class Beans {

        private int number;
        private int type;
        private int pos;

        public Beans(int pos, int type, int number) {
            this.pos = pos;
            this.number = number;
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public int getNumber() {
            return number;
        }

        public int getPos() {
            return pos;
        }
    }

    public static final void BeanGame2(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getSession().write(MaplePacketCreator.updateBeans(c.getPlayer().getId(), c.getPlayer().getBeans()));
        c.getSession().write(MaplePacketCreator.enableActions());
    }
}
