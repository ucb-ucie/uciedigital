package edu.berkeley.cs.ucie.digital.d2dadapter

import chisel3._
import chisel3.util._


class CRC16Lookup {

    // Lookup table for CRC-16 (0x8005) polynomial

    val table = VecInit(0x0000.U, 0x8005.U, 0x800F.U, 0x000A.U, 0x801B.U, 0x001E.U, 0x0014.U, 0x8011.U, 
      0x8033.U, 0x0036.U, 0x003C.U, 0x8039.U, 0x0028.U, 0x802D.U, 0x8027.U, 0x0022.U, 
      0x8063.U, 0x0066.U, 0x006C.U, 0x8069.U, 0x0078.U, 0x807D.U, 0x8077.U, 0x0072.U, 
      0x0050.U, 0x8055.U, 0x805F.U, 0x005A.U, 0x804B.U, 0x004E.U, 0x0044.U, 0x8041.U, 
      0x80C3.U, 0x00C6.U, 0x00CC.U, 0x80C9.U, 0x00D8.U, 0x80DD.U, 0x80D7.U, 0x00D2.U, 
      0x00F0.U, 0x80F5.U, 0x80FF.U, 0x00FA.U, 0x80EB.U, 0x00EE.U, 0x00E4.U, 0x80E1.U, 
      0x00A0.U, 0x80A5.U, 0x80AF.U, 0x00AA.U, 0x80BB.U, 0x00BE.U, 0x00B4.U, 0x80B1.U, 
      0x8093.U, 0x0096.U, 0x009C.U, 0x8099.U, 0x0088.U, 0x808D.U, 0x8087.U, 0x0082.U, 
      0x8183.U, 0x0186.U, 0x018C.U, 0x8189.U, 0x0198.U, 0x819D.U, 0x8197.U, 0x0192.U, 
      0x01B0.U, 0x81B5.U, 0x81BF.U, 0x01BA.U, 0x81AB.U, 0x01AE.U, 0x01A4.U, 0x81A1.U, 
      0x01E0.U, 0x81E5.U, 0x81EF.U, 0x01EA.U, 0x81FB.U, 0x01FE.U, 0x01F4.U, 0x81F1.U, 
      0x81D3.U, 0x01D6.U, 0x01DC.U, 0x81D9.U, 0x01C8.U, 0x81CD.U, 0x81C7.U, 0x01C2.U, 
      0x0140.U, 0x8145.U, 0x814F.U, 0x014A.U, 0x815B.U, 0x015E.U, 0x0154.U, 0x8151.U, 
      0x8173.U, 0x0176.U, 0x017C.U, 0x8179.U, 0x0168.U, 0x816D.U, 0x8167.U, 0x0162.U, 
      0x8123.U, 0x0126.U, 0x012C.U, 0x8129.U, 0x0138.U, 0x813D.U, 0x8137.U, 0x0132.U, 
      0x0110.U, 0x8115.U, 0x811F.U, 0x011A.U, 0x810B.U, 0x010E.U, 0x0104.U, 0x8101.U, 
      0x8303.U, 0x0306.U, 0x030C.U, 0x8309.U, 0x0318.U, 0x831D.U, 0x8317.U, 0x0312.U, 
      0x0330.U, 0x8335.U, 0x833F.U, 0x033A.U, 0x832B.U, 0x032E.U, 0x0324.U, 0x8321.U, 
      0x0360.U, 0x8365.U, 0x836F.U, 0x036A.U, 0x837B.U, 0x037E.U, 0x0374.U, 0x8371.U, 
      0x8353.U, 0x0356.U, 0x035C.U, 0x8359.U, 0x0348.U, 0x834D.U, 0x8347.U, 0x0342.U, 
      0x03C0.U, 0x83C5.U, 0x83CF.U, 0x03CA.U, 0x83DB.U, 0x03DE.U, 0x03D4.U, 0x83D1.U, 
      0x83F3.U, 0x03F6.U, 0x03FC.U, 0x83F9.U, 0x03E8.U, 0x83ED.U, 0x83E7.U, 0x03E2.U, 
      0x83A3.U, 0x03A6.U, 0x03AC.U, 0x83A9.U, 0x03B8.U, 0x83BD.U, 0x83B7.U, 0x03B2.U, 
      0x0390.U, 0x8395.U, 0x839F.U, 0x039A.U, 0x838B.U, 0x038E.U, 0x0384.U, 0x8381.U, 
      0x0280.U, 0x8285.U, 0x828F.U, 0x028A.U, 0x829B.U, 0x029E.U, 0x0294.U, 0x8291.U, 
      0x82B3.U, 0x02B6.U, 0x02BC.U, 0x82B9.U, 0x02A8.U, 0x82AD.U, 0x82A7.U, 0x02A2.U, 
      0x82E3.U, 0x02E6.U, 0x02EC.U, 0x82E9.U, 0x02F8.U, 0x82FD.U, 0x82F7.U, 0x02F2.U, 
      0x02D0.U, 0x82D5.U, 0x82DF.U, 0x02DA.U, 0x82CB.U, 0x02CE.U, 0x02C4.U, 0x82C1.U, 
      0x8243.U, 0x0246.U, 0x024C.U, 0x8249.U, 0x0258.U, 0x825D.U, 0x8257.U, 0x0252.U, 
      0x0270.U, 0x8275.U, 0x827F.U, 0x027A.U, 0x826B.U, 0x026E.U, 0x0264.U, 0x8261.U, 
      0x0220.U, 0x8225.U, 0x822F.U, 0x022A.U, 0x823B.U, 0x023E.U, 0x0234.U, 0x8231.U, 
      0x8213.U, 0x0216.U, 0x021C.U, 0x8219.U, 0x0208.U, 0x820D.U, 0x8207.U, 0x0202.U)




}