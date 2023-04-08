package com.mr.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import com.mr.modle.*;
import com.mr.service.*;
/**
 * ��Ϸ���
 * 
 * @author 格伦的奇妙冒险
 *
 */
public class GamePanel extends JPanel implements KeyListener {
    private BufferedImage image;// ��ͼƬ
    private BackgroundImage background;// ����ͼƬ
    private Dinosaur golden;// ����
    private Graphics2D g2;// ��ͼƬ��ͼ����
    private int addObstacleTimer = 0;// ����ϰ���ʱ��
    private boolean finish = false;// ��Ϸ������־
    private List<Obstacle> list = new ArrayList<Obstacle>();// �ϰ�����
    private final int FREASH = FreshThread.FREASH;// ˢ��ʱ��

    int score = 0;// �÷�
    int scoreTimer = 0;// ������ʱ��

    public GamePanel() {
        // ��ͼƬ���ÿ�800��300�Ĳ�ɫͼƬ
        image = new BufferedImage(800, 300, BufferedImage.TYPE_INT_BGR);
        g2 = image.createGraphics();// ��ȡ��ͼƬ��ͼ����
        background = new BackgroundImage();// ��ʼ����������
        golden = new Dinosaur();// ��ʼ��С����
        list.add(new Obstacle());// ��ӵ�һ���ϰ�
        FreshThread t = new FreshThread(this);// ˢ��֡�߳�
        t.start();// �����߳�
    }

    /**
     * ������ͼƬ
     */
    private void paintImage() {
        background.roll();// ����ͼƬ��ʼ����
        golden.move();// ������ʼ�ƶ�
        g2.drawImage(background.image, 0, 0, this);// ���ƹ�������
        if (addObstacleTimer == 1300) {// ÿ��1300����
            if (Math.random() * 100 > 40) {// 60%���ʳ����ϰ�
                list.add(new Obstacle());
            }
            addObstacleTimer = 0;// ���¼�ʱ
        }

        for (int i = 0; i < list.size(); i++) {// �����ϰ�����
            Obstacle o = list.get(i);// ��ȡ�ϰ�����
            if (o.isLive()) {// �������Ч�ϰ�
                o.move();// �ϰ��ƶ�
                g2.drawImage(o.image, o.x, o.y, this);// �����ϰ�
                // �������ͷ�������ϰ�
                if (o.getBounds().intersects(golden.getFootBounds())
                        || o.getBounds().intersects(golden.getHeadBounds())) {
                    Sound.hit();// ����ײ������
                    gameOver();// ��Ϸ����
                }
            } else {// ���������Ч�ϰ�
                list.remove(i);// ɾ�����ϰ�
                i--;// ѭ������ǰ��
            }
        }
        g2.drawImage(golden.image, golden.x, golden.y, this);// ���ƿ���
        if (scoreTimer >= 500) {// ÿ��500����
            score += 10;// ��ʮ��
            scoreTimer = 0;// ���¼�ʱ
        }

        g2.setColor(Color.BLACK);// ʹ�ú�ɫ
        g2.setFont(new Font("����", Font.BOLD, 24));// ��������
        g2.drawString(String.format("%06d", score), 700, 30);// ���Ʒ���

        addObstacleTimer += FREASH;// �ϰ���ʱ������
        scoreTimer += FREASH;// ������ʱ������
    }

    /**
     * ��д�����������
     */
    public void paint(Graphics g) {
        paintImage();// ������ͼƬ����
        g.drawImage(image, 0, 0, this);
    }

    /**
     * ��Ϸ�Ƿ����
     * 
     * @return
     */
    public boolean isFinish() {
        return finish;
    }

    /**
     * ʹ��Ϸ����
     */
    public void gameOver() {
        ScoreRecorder.addNewScore(score);// ��¼��ǰ����
        finish = true;
    }

    /**
     * ʵ�ְ��¼��̰�������
     */
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();// ��ȡ���µİ���ֵ
        if (code == KeyEvent.VK_SPACE) {// ����ǿո�
            golden.jump();// ������Ծ
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}