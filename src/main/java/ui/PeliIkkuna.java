/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Random;
import blackjack.Jakaja;
import blackjack.Kortti;
import blackjack.Maa;
import blackjack.Pelaaja;

/**
 *
 * @author kubuntu
 */
public class PeliIkkuna extends javax.swing.JFrame {

    private ArrayList<Kortti> kortit;
    private ArrayList<Kortti> jaljella;
    private Random random;
    private Pelaaja pelaaja;
    private Jakaja jakaja;
    private Boolean lopetettu;
    private int jakajanMaksimi;
    private int balance;
    private int bet;
    private boolean currentlyBetting;

    /**
     * Creates new form PeliIkkuna
     */
    public PeliIkkuna() {
        this(17);
    }

    public PeliIkkuna(int jakajanMaksimi) {
        this.jakajanMaksimi = jakajanMaksimi;
        this.balance = 10000;
        this.bet = 0;
        currentlyBetting = true;
        initComponents();
        hiddenResetMoneyButton.show(false);
        betButton.show(false);
    }

    public ArrayList<Kortti> luoKortit(ArrayList lista, int montako) {
        for (int pakat = 0; pakat < montako; pakat++) {
            for (Maa maa : Maa.values()) {
                for (int i = 1; i < 14; i++) {
                    lista.add(new Kortti(maa, i));
                }
            }
        }

        return lista;
    }

    public Kortti nostaKortti() {
        int indeksi = random.nextInt(jaljella.size());
        Kortti kortti = jaljella.get(indeksi);
        jaljella.remove(indeksi);
        return kortti;
    }

    public void jakajaHit() {
        Kortti kortti = pelaajaHit(this.jakaja);
        jakajanKasi.setText(jakajanKasi.getText() + " " + kortti);
    }

    public void pelaajaHit() {
        Kortti kortti = pelaajaHit(this.pelaaja);
        pelaajanKasi.setText(pelaajanKasi.getText() + " " + kortti);
    }

    public Kortti pelaajaHit(Pelaaja pelaaja) {
        Kortti kortti = nostaKortti();
        pelaaja.lisaaKortti(kortti);
        asetaSummat();
        return kortti;
    }

    public void asetaSummat() {
        if (this.jakaja.yli()) {
            this.jakajanSumma.setText("OVER");
        } else {
            this.jakajanSumma.setText("=" + this.jakaja.getSummat());
        }
        if (this.pelaaja.yli()) {
            this.pelaajanSumma.setText("OVER");
        } else {
            this.pelaajanSumma.setText("=" + this.pelaaja.getSummat());
        }
    }
    
    public void asetaLopullisetSummat() {
        if (this.jakaja.yli()) {
            this.jakajanSumma.setText("OVER");
        } else {
            this.jakajanSumma.setText("=" + String.valueOf(this.jakaja.getMaxSumma()));
        }
        if (this.pelaaja.yli()) {
            this.pelaajanSumma.setText("OVER");
        } else {
            this.pelaajanSumma.setText("=" + String.valueOf(this.pelaaja.getMaxSumma()));
        }
    }

    public void betPhase() {
        hitButton.setEnabled(false);
        stayButton.setEnabled(false);
        doubleButton.setEnabled(false);
        bet20euro.setEnabled(true);
        bet10euro.setEnabled(true);
        bet5euro.setEnabled(true);
        bet2euro.setEnabled(true);
        bet1euro.setEnabled(true);
        bet50cents.setEnabled(true);
        betReset.setEnabled(true);
        currentlyBetting = true;
    }

    public void aloita() {
        restart();
        currentlyBetting = false;
        bet20euro.setEnabled(false);
        bet10euro.setEnabled(false);
        bet5euro.setEnabled(false);
        bet2euro.setEnabled(false);
        bet1euro.setEnabled(false);
        bet50cents.setEnabled(false);
        betReset.setEnabled(false);
        restartButton.setEnabled(false);
        this.balance = this.balance - this.bet;
        bet(0);
        winner.setText("");
        kortit = new ArrayList<>();
        random = new Random();
        luoKortit(kortit, 5);
        jaljella = new ArrayList<>(kortit);
        jakaja = new Jakaja(this.jakajanMaksimi);
        pelaaja = new Pelaaja();
        lopetettu = false;
        pelaajanKasi.setText("");
        jakajanKasi.setText("");
        hitButton.setEnabled(true);
        hitButton.setText("Hit");
        stayButton.setEnabled(true);
        if (this.balance >= this.bet) {
            doubleButton.setEnabled(true);
        } else {
            doubleButton.setEnabled(false);
        }
        pelaajaHit();
        jakajaHit();
        pelaajaHit();
        asetaSummat();
        if (this.pelaaja.getMaxSumma() >= 21) {
            lopeta();
        }

    }

    public void poistaNapit() {
        doubleButton.setEnabled(false);
        hitButton.setEnabled(false);
        stayButton.setEnabled(false);
        restartButton.setEnabled(true);
        betButton.setEnabled(true);
    }

    public void lopeta() {
        poistaNapit();
        if (!this.pelaaja.yli()) {
            while (this.jakaja.canHit()) {
                jakajaHit();
            }
        }
        if (this.jakaja.yli()) {
            voittaja(this.pelaaja);
        } else if (this.pelaaja.yli()) {
            voittaja(this.jakaja);
        } else if (this.jakaja.getMaxSumma() > this.pelaaja.getMaxSumma()) {
            voittaja(this.jakaja);
        } else if (this.pelaaja.getMaxSumma() > this.jakaja.getMaxSumma()) {
            voittaja(this.pelaaja);
        } else {
            tasapeli();
        }
        asetaLopullisetSummat();
        lopetettu = true;
        restart();
    }

    public void bet(int summa) {;
        if (this.balance >= summa) {
            if (testBalance(-summa)) {
                this.bet = this.bet + summa;
            }
        } else {
            System.out.println("Too much!");
        }
        bets.setText((Double.valueOf(this.bet) / 100) + " €");
        money.setText((Double.valueOf(this.balance) / 100) + " €");
    }

    public boolean testBalance(int summa) {
        boolean ableTo = false;
        if (summa >= 0) {
        } else if (summa < 0) {
            if (this.balance >= abs(summa) + this.bet) {
                ableTo = true;
            } else {
                System.out.println("Too much!");
            }
        }
        return ableTo;
    }

    public void resetBets() {
        this.bet = 0;
        bet(0);
    }

    public void restart() {
        restartButton.setText("Restart");
        restartButton.show(true);
        restartButton.setEnabled(true);
        if (this.balance == 0 && this.bet == 0) {
            hiddenResetMoneyButton.setEnabled(true);
            hiddenResetMoneyButton.show(true);
        }
        betPhase();
    }

    public void voittaja(Pelaaja pelaaja) {
        System.out.println(pelaaja);
        if (pelaaja.equals(this.pelaaja)) {
            winner.setText("YOU WIN!");
            this.balance = this.balance + 2 * this.bet;
            int lastBet = this.bet;
            this.bet = 0;
            bet(lastBet);
        } else {
            winner.setText("YOU LOSE!");
            int lastBet = this.bet;
            this.bet = 0;
            bet(lastBet);
        }
    }

    public void tasapeli() {
        winner.setText("TIE!");
        this.balance = this.balance + this.bet;
        int lastBet = this.bet;
        this.bet = 0;
        bet(lastBet);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        doubleButton = new javax.swing.JButton();
        hitButton = new javax.swing.JButton();
        stayButton = new javax.swing.JButton();
        pelaajanKasi = new javax.swing.JLabel();
        jakajanKasi = new javax.swing.JLabel();
        pelaajanSumma = new javax.swing.JLabel();
        jakajanSumma = new javax.swing.JLabel();
        restartButton = new javax.swing.JButton();
        winner = new javax.swing.JLabel();
        money = new javax.swing.JLabel();
        betReset = new javax.swing.JButton();
        bet50cents = new javax.swing.JButton();
        bet1euro = new javax.swing.JButton();
        bet2euro = new javax.swing.JButton();
        bet5euro = new javax.swing.JButton();
        bet10euro = new javax.swing.JButton();
        bet20euro = new javax.swing.JButton();
        bets = new javax.swing.JLabel();
        betButton = new javax.swing.JButton();
        hiddenResetMoneyButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Blackjack");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(1000, 800));
        setResizable(false);

        doubleButton.setText("Double");
        doubleButton.setEnabled(false);
        doubleButton.setMaximumSize(new java.awt.Dimension(80, 27));
        doubleButton.setMinimumSize(new java.awt.Dimension(80, 27));
        doubleButton.setPreferredSize(new java.awt.Dimension(80, 27));
        doubleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doubleButtonActionPerformed(evt);
            }
        });

        hitButton.setText("Hit");
        hitButton.setEnabled(false);
        hitButton.setMaximumSize(new java.awt.Dimension(80, 27));
        hitButton.setMinimumSize(new java.awt.Dimension(80, 27));
        hitButton.setPreferredSize(new java.awt.Dimension(80, 27));
        hitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hitButtonActionPerformed(evt);
            }
        });

        stayButton.setText("Stay");
        stayButton.setEnabled(false);
        stayButton.setMaximumSize(new java.awt.Dimension(80, 27));
        stayButton.setMinimumSize(new java.awt.Dimension(80, 27));
        stayButton.setPreferredSize(new java.awt.Dimension(80, 27));
        stayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stayButtonActionPerformed(evt);
            }
        });

        pelaajanKasi.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N

        jakajanKasi.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N

        pelaajanSumma.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N

        jakajanSumma.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N

        restartButton.setText("Start");
        restartButton.setMaximumSize(new java.awt.Dimension(80, 27));
        restartButton.setMinimumSize(new java.awt.Dimension(80, 27));
        restartButton.setPreferredSize(new java.awt.Dimension(80, 27));
        restartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartButtonActionPerformed(evt);
            }
        });

        winner.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N

        money.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        money.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        money.setText("100.0 €");

        betReset.setText("Reset");
        betReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                betResetActionPerformed(evt);
            }
        });

        bet50cents.setText("50 snt");
        bet50cents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bet50centsActionPerformed(evt);
            }
        });

        bet1euro.setText("1 €");
        bet1euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bet1euroActionPerformed(evt);
            }
        });

        bet2euro.setText("2 €");
        bet2euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bet2euroActionPerformed(evt);
            }
        });

        bet5euro.setText("5 €");
        bet5euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bet5euroActionPerformed(evt);
            }
        });

        bet10euro.setText("10 €");
        bet10euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bet10euroActionPerformed(evt);
            }
        });

        bet20euro.setText("20 €");
        bet20euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bet20euroActionPerformed(evt);
            }
        });

        bets.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        bets.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bets.setText("0.0 €");

        betButton.setText("Bet");
        betButton.setEnabled(false);
        betButton.setMaximumSize(new java.awt.Dimension(80, 27));
        betButton.setMinimumSize(new java.awt.Dimension(80, 27));
        betButton.setPreferredSize(new java.awt.Dimension(80, 27));
        betButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                betButtonActionPerformed(evt);
            }
        });

        hiddenResetMoneyButton.setText("Money");
        hiddenResetMoneyButton.setEnabled(false);
        hiddenResetMoneyButton.setMaximumSize(new java.awt.Dimension(80, 27));
        hiddenResetMoneyButton.setMinimumSize(new java.awt.Dimension(80, 27));
        hiddenResetMoneyButton.setPreferredSize(new java.awt.Dimension(80, 27));
        hiddenResetMoneyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hiddenResetMoneyButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pelaajanKasi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                            .addComponent(jakajanKasi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(doubleButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(betButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(135, 135, 135)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(restartButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(hitButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(135, 135, 135)
                                        .addComponent(stayButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(hiddenResetMoneyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(winner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jakajanSumma, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                            .addComponent(pelaajanSumma, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(betReset, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(bet50cents, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(bet1euro, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(bet2euro, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(bet5euro, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(bet10euro, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(bet20euro, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(money, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bets, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 210, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(winner, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jakajanKasi, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jakajanSumma, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pelaajanKasi, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pelaajanSumma, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(hiddenResetMoneyButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(doubleButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hitButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stayButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(restartButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(betButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(money, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bets, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(betReset, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bet50cents, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bet1euro, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bet2euro, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bet5euro, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bet10euro, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bet20euro, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void stayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stayButtonActionPerformed
        // TODO add your handling code here:
        lopeta();
    }//GEN-LAST:event_stayButtonActionPerformed

    private void doubleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doubleButtonActionPerformed
        // TODO add your handling code here:
        this.balance = this.balance - this.bet;
        bet(this.bet);
        hitButton.doClick();
        if (!lopetettu) {
            lopeta();
        }
    }//GEN-LAST:event_doubleButtonActionPerformed

    private void hitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hitButtonActionPerformed
        // TODO add your handling code here:
        doubleButton.setEnabled(false);
//        doubleButton.show(false);
        pelaajaHit();
        if (this.pelaaja.getMaxSumma() >= 21) {
            lopeta();
        }
    }//GEN-LAST:event_hitButtonActionPerformed

    private void restartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartButtonActionPerformed
        // TODO add your handling code here:
        if (this.bet >= 100) {
            aloita();
        } else {
            winner.setText("Bet at least 1 €");
        }
    }//GEN-LAST:event_restartButtonActionPerformed

    private void bet20euroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bet20euroActionPerformed
        // TODO add your handling code here:
        bet(2000);
    }//GEN-LAST:event_bet20euroActionPerformed

    private void betResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_betResetActionPerformed
        // TODO add your handling code here:
        resetBets();
    }//GEN-LAST:event_betResetActionPerformed

    private void bet50centsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bet50centsActionPerformed
        // TODO add your handling code here:
        bet(50);
    }//GEN-LAST:event_bet50centsActionPerformed

    private void bet1euroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bet1euroActionPerformed
        // TODO add your handling code here:
        bet(100);
    }//GEN-LAST:event_bet1euroActionPerformed

    private void bet2euroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bet2euroActionPerformed
        // TODO add your handling code here:
        bet(200);
    }//GEN-LAST:event_bet2euroActionPerformed

    private void bet5euroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bet5euroActionPerformed
        // TODO add your handling code here:
        bet(500);
    }//GEN-LAST:event_bet5euroActionPerformed

    private void bet10euroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bet10euroActionPerformed
        // TODO add your handling code here:
        bet(1000);
    }//GEN-LAST:event_bet10euroActionPerformed

    private void betButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_betButtonActionPerformed
        // TODO add your handling code here:
        betPhase();
    }//GEN-LAST:event_betButtonActionPerformed

    private void hiddenResetMoneyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hiddenResetMoneyButtonActionPerformed
        // TODO add your handling code here:
        this.balance = 10000;
        bet(0);
        hiddenResetMoneyButton.setEnabled(false);
        hiddenResetMoneyButton.show(false);
    }//GEN-LAST:event_hiddenResetMoneyButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PeliIkkuna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PeliIkkuna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PeliIkkuna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PeliIkkuna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PeliIkkuna().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bet10euro;
    private javax.swing.JButton bet1euro;
    private javax.swing.JButton bet20euro;
    private javax.swing.JButton bet2euro;
    private javax.swing.JButton bet50cents;
    private javax.swing.JButton bet5euro;
    private javax.swing.JButton betButton;
    private javax.swing.JButton betReset;
    private javax.swing.JLabel bets;
    private javax.swing.JButton doubleButton;
    private javax.swing.JButton hiddenResetMoneyButton;
    private javax.swing.JButton hitButton;
    private javax.swing.JLabel jakajanKasi;
    private javax.swing.JLabel jakajanSumma;
    private javax.swing.JLabel money;
    private javax.swing.JLabel pelaajanKasi;
    private javax.swing.JLabel pelaajanSumma;
    private javax.swing.JButton restartButton;
    private javax.swing.JButton stayButton;
    private javax.swing.JLabel winner;
    // End of variables declaration//GEN-END:variables
}
