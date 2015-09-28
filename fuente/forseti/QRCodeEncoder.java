/*
    Forseti, El ERP Gratuito para PyMEs
    Copyright (C) 2015 Gabriel Gutiérrez Fuentes.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package forseti;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Clase encargada del proceso de codificación QRCode.
 * @author Ginés Miguel Fernández Ordóñez
 * @modified El equipo forseti
 */
public class QRCodeEncoder
{
    /**
     * Nivel de corrección de errores
     */
    private int qrcodeErrorCorrect;

    /**
     * Modo de codificación
     */
    private int qrcodeEncodeMode;

    private int qrcodeVersion;
	
    private int qrcodeStructureappendN;
    private int qrcodeStructureappendM;
    private int qrcodeStructureappendParity;

    /**
     * Color de fondo
     */
    private Color qrCodeBackgroundColor;

    /**
     * Color de módulos
     */
    private Color qrCodeForegroundColor;

    /**
     * ancho en pixeles de cada módulo
     */
    private int qrCodeScale;

    //private String qrcodeStructureappendOriginaldata;
	
    /**
     * Constructor, establece parámetros por defecto.
     */
    public QRCodeEncoder()
    {
        qrcodeErrorCorrect = QRCodeConstantes.ErrorCorrectM;
        qrcodeEncodeMode = QRCodeConstantes.EncodeModeByte;
        qrcodeVersion = 7;

        qrcodeStructureappendN = 0;
        qrcodeStructureappendM = 0;
        qrcodeStructureappendParity = 0;
        //qrcodeStructureappendOriginaldata = "";

        qrCodeScale = 4;
        qrCodeBackgroundColor = Color.WHITE; //Color.white
        qrCodeForegroundColor = Color.BLACK;
    }

    /**
     * Obtiene el color de fondo del símbolo.
     * @return Color
     */
    public Color getQrCodeBackgroundColor()
    {
        return qrCodeBackgroundColor;
    }

    /**
     * Establece el color de fondo del símbolo.
     * @param qrCodeBackgroundColor
     */
    public void setQrCodeBackgroundColor(Color qrCodeBackgroundColor)
    {
        this.qrCodeBackgroundColor = qrCodeBackgroundColor;
    }

    /**
     * Obtiene el modo de codificación.
     * @return int
     */
    public int getQrcodeEncodeMode()
    {
        return qrcodeEncodeMode;
    }

    /**
     * Establece el modo de codificación.
     * @param qrcodeEncodeMode
     */
    public void setQrcodeEncodeMode(int qrcodeEncodeMode)
    {
        this.qrcodeEncodeMode = qrcodeEncodeMode;
    }

    /**
     * Obtiene el nivel de corrección de errores.
     * @return int
     */
    public int getQrcodeErrorCorrect()
    {
        return qrcodeErrorCorrect;
    }

    /**
     * Establece el nivel de corrección de errores.
     * @param qrcodeErrorCorrect
     */
    public void setQrcodeErrorCorrect(int qrcodeErrorCorrect)
    {
        this.qrcodeErrorCorrect = qrcodeErrorCorrect;
    }

    /**
     * Obtiene el color de módulo.
     * @return Color
     */
    public Color getQrCodeForegroundColor()
    {
        return qrCodeForegroundColor;
    }

    /**
     * Establece el color de módulo.
     * @param qrCodeForegroundColor
     */
    public void setQrCodeForegroundColor(Color qrCodeForegroundColor)
    {
        this.qrCodeForegroundColor = qrCodeForegroundColor;
    }

    /**
     * Obtiene la escala de módulo.
     * @return int
     */
    public int getQrCodeScale()
    {
        return qrCodeScale;
    }

    /**
     * Establece la escala de módulo.
     * @param qrCodeScale
     */
    public void setQrCodeScale(int qrCodeScale)
    {
        this.qrCodeScale = qrCodeScale;
    }

    /**
     * Obtiene la versión del QRCode.
     * @return int
     */
    public int getQrcodeVersion()
    {
        return qrcodeVersion;
    }
    
    /**
     * Establece la versión del QRCode.
     * @param qrcodeVersion
     */
    public void setQrcodeVersion(int qrcodeVersion)
    {
        this.qrcodeVersion = qrcodeVersion;
    }
	
    /**
     * Establece los valores para crear QRCodes structure append.
     * @param m valor m de structure append (1-16)
     * @param n valor n de structure append (2-16)
     * @param p paridad (0-255)
     */
    public void setStructureappend(int m, int n, int p)
    {
        if (n > 1 && n <= 16 && m > 0 && m <= 16 && p >= 0 && p <= 255)
        {
                qrcodeStructureappendM = m;
                qrcodeStructureappendN = n;
                qrcodeStructureappendParity = p;
        }
    }
	
    /**
     * Calcula la paridad para structure append en función de los datos a codificar.
     * @param originaldata byte[] datos
     * @return int structure append paridad
     */
    public int calStructureappendParity(byte[] originaldata)
    {
        int originaldataLength;
        int i = 0;
        int structureappendParity = 0;

        originaldataLength = originaldata.length;

        if (originaldataLength > 1)
        {
                structureappendParity = 0;
                while (i < originaldataLength)
                {
                        structureappendParity = (structureappendParity ^ (originaldata[i] & 0xFF));
                        i++;
                }
        }
        else
        {
                structureappendParity = - 1;
        }
        return structureappendParity;
    }

    /**
     * Crea una matriz de booleanos que representa a un símbolo QRCode que
     * codifica la informacion recibida como datos de entrada.
     * @param qrcodeData byte[] con los datos a codificar
     * @return boolean[][] matriz con el símbolo QRCode
     */
    public boolean[][] calQrcode(byte[] qrcodeData)
    {
        int dataLength;
        int dataCounter = 0;
        dataLength = qrcodeData.length;
        int[] dataValue = new int[dataLength + 32];
        byte[] dataBits = new byte[dataLength + 32];

        if (dataLength <= 0)
        {
                boolean[][] ret = new boolean[][]{new boolean[]{false}};//matriz de falses
                return ret;
        }

        if (qrcodeStructureappendN > 1)//usa structure append
        {
                dataValue[0] = 3;
                dataBits[0] = 4;

                dataValue[1] = qrcodeStructureappendM - 1;
                dataBits[1] = 4;

                dataValue[2] = qrcodeStructureappendN - 1;
                dataBits[2] = 4;

                dataValue[3] = qrcodeStructureappendParity;
                dataBits[3] = 8;

                dataCounter = 4;
        }
        dataBits[dataCounter] = 4;

        // determinando el modo de codificación

        int[] codewordNumPlus;
        int codewordNumCounterValue;

        switch (qrcodeEncodeMode)
        {
                // Modo Alfanumérico
        case QRCodeConstantes.EncodeModeAlphanumeric:
            codewordNumPlus = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};

            dataValue[dataCounter] = 2;
            dataCounter++;
            dataValue[dataCounter] = dataLength;
            dataBits[dataCounter] = 9;
            codewordNumCounterValue = dataCounter;

            dataCounter++;
            for (int i = 0; i < dataLength; i++)
            {
                    char chr = (char) qrcodeData[i];
                    byte chrValue = 0;
                    if (chr >= 48 && chr < 58) //numérico
                    {
                            chrValue = (byte) (chr - 48);
                    }
                    else
                    {
                            if (chr >= 65 && chr < 91) //mayúsculas
                            {
                                    chrValue = (byte) (chr - 55);
                            }
                            else
                            {
                                    if (chr == 32)
                                    {
                                            chrValue = 36;
                                    }
                                    if (chr == 36)
                                    {
                                            chrValue = 37;
                                    }
                                    if (chr == 37)
                                    {
                                            chrValue = 38;
                                    }
                                    if (chr == 42)
                                    {
                                            chrValue = 39;
                                    }
                                    if (chr == 43)
                                    {
                                            chrValue = 40;
                                    }
                                    if (chr == 45)
                                    {
                                            chrValue = 41;
                                    }
                                    if (chr == 46)
                                    {
                                            chrValue = 42;
                                    }
                                    if (chr == 47)
                                    {
                                            chrValue = 43;
                                    }
                                    if (chr == 58)
                                    {
                                            chrValue = 44;
                                    }
                            }
                    }
                    if ((i % 2) == 0)
                    {
                            dataValue[dataCounter] = chrValue;
                            dataBits[dataCounter] = 6;
                    }
                    else
                    {
                            dataValue[dataCounter] = dataValue[dataCounter] * 45 + chrValue;
                            dataBits[dataCounter] = 11;
                            if (i < dataLength - 1)
                            {
                                    dataCounter++;
                            }
                    }
            }
            dataCounter++;
            break;
        //Modo numérico
        case QRCodeConstantes.EncodeModeNumeric:
            codewordNumPlus = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
            dataValue[dataCounter] = 1;
            dataCounter++;
            dataValue[dataCounter] = dataLength;
            dataBits[dataCounter] = 10; // versión 1-9
            codewordNumCounterValue = dataCounter;
            dataCounter++;
            for (int i = 0; i < dataLength; i++)
            {
                    if ((i % 3) == 0)
                    {
                        dataValue[dataCounter] = (int) (qrcodeData[i] - 0x30);
                        dataBits[dataCounter] = 4;
                    }
                    else
                    {
                        dataValue[dataCounter] = dataValue[dataCounter] * 10 + (int) (qrcodeData[i] - 0x30);

                        if ((i % 3) == 1)
                        {
                                dataBits[dataCounter] = 7;
                        }
                        else
                        {
                                dataBits[dataCounter] = 10;
                                if (i < dataLength - 1)
                                {
                                        dataCounter++;
                                }
                        }
                    }
            }
            dataCounter++;
            break;
        //: Kanji Mode
        //Modo Byte
        default:
            codewordNumPlus = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8};
            dataValue[dataCounter] = 4;
            dataCounter++;
            dataValue[dataCounter] = dataLength;
            dataBits[dataCounter] = 8; // versión 1-9
            codewordNumCounterValue = dataCounter;
            dataCounter++;
            for (int i = 0; i < dataLength; i++)
            {
                    dataValue[i + dataCounter] = (qrcodeData[i] & 0xFF);
                    dataBits[i + dataCounter] = 8;
            }
            dataCounter += dataLength;
            break;
        }

        int totalDataBits = 0;
        for (int i = 0; i < dataCounter; i++)
        {
                totalDataBits += dataBits[i];
        }
        int ec;
        switch (qrcodeErrorCorrect)
        {
            case QRCodeConstantes.ErrorCorrectL:
                ec = 1;
                break;
            case QRCodeConstantes.ErrorCorrectQ:
                ec = 3;
                break;
            case QRCodeConstantes.ErrorCorrectH:
                ec = 2;
                break;
            default: //M
                ec = 0;
                break;
        }
        int[][] maxDataBitsArray = new int[][]{new int[]{0, 128, 224, 352, 512, 688, 864, 992, 1232, 1456, 1728, 2032, 2320, 2672, 2920, 3320, 3624, 4056, 4504, 5016, 5352, 5712, 6256, 6880, 7312, 8000, 8496, 9024, 9544, 10136, 10984, 11640, 12328, 13048, 13800, 14496, 15312, 15936, 16816, 17728, 18672}, new int[]{0, 152, 272, 440, 640, 864, 1088, 1248, 1552, 1856, 2192, 2592, 2960, 3424, 3688, 4184, 4712, 5176, 5768, 6360, 6888, 7456, 8048, 8752, 9392, 10208, 10960, 11744, 12248, 13048, 13880, 14744, 15640, 16568, 17528, 18448, 19472, 20528, 21616, 22496, 23648}, new int[]{0, 72, 128, 208, 288, 368, 480, 528, 688, 800, 976, 1120, 1264, 1440, 1576, 1784, 2024, 2264, 2504, 2728, 3080, 3248, 3536, 3712, 4112, 4304, 4768, 5024, 5288, 5608, 5960, 6344, 6760, 7208, 7688, 7888, 8432, 8768, 9136, 9776, 10208}, new int[]{0, 104, 176, 272, 384, 496, 608, 704, 880, 1056, 1232, 1440, 1648, 1952, 2088, 2360, 2600, 2936, 3176, 3560, 3880, 4096, 4544, 4912, 5312, 5744, 6032, 6464, 6968, 7288, 7880, 8264, 8920, 9368, 9848, 10288, 10832, 11408, 12016, 12656, 13328}};
        int maxDataBits = 0;
        if (qrcodeVersion == 0)
        {
            // versión automática
            qrcodeVersion = 1;
            for (int i = 1; i <= 40; i++)
            {
                    if ((maxDataBitsArray[ec][i]) >= totalDataBits + codewordNumPlus[qrcodeVersion])
                    {
                            maxDataBits = maxDataBitsArray[ec][i];
                            break;
                    }
                    qrcodeVersion++;
            }
        }
        else
        {
            maxDataBits = maxDataBitsArray[ec][qrcodeVersion];
        }
        totalDataBits += codewordNumPlus[qrcodeVersion];
        dataBits[codewordNumCounterValue] = (byte) (dataBits[codewordNumCounterValue] + codewordNumPlus[qrcodeVersion]);
        int[] maxCodewordsArray = new int[]{0, 26, 44, 70, 100, 134, 172, 196, 242, 292, 346, 404, 466, 532, 581, 655, 733, 815, 901, 991, 1085, 1156, 1258, 1364, 1474, 1588, 1706, 1828, 1921, 2051, 2185, 2323, 2465, 2611, 2761, 2876, 3034, 3196, 3362, 3532, 3706};
        int maxCodewords = maxCodewordsArray[qrcodeVersion];
        //int maxModules1side = 17 + (qrcodeVersion << 2);
        int[] matrixRemainBit = new int[]{0, 0, 7, 7, 7, 7, 7, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0};

        // Leyendo versión ECC del fichero de datos
        int byte_num = matrixRemainBit[qrcodeVersion] + (maxCodewords << 3);
        byte[] matrixX = new byte[byte_num];
        byte[] matrixY = new byte[byte_num];
        byte[] maskArray = new byte[byte_num];
        byte[] formatInformationX2 = new byte[15];
        byte[] formatInformationY2 = new byte[15];
        byte[] rsEccCodewords = new byte[1];
        byte[] rsBlockOrderTemp = new byte[128];

        try
        {
            String fileName = "qrv" + qrcodeVersion + "_" + ec + ".dat";
            File file;
            file = new File("/usr/local/forseti/rec/" + fileName);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            JUtil.ReadInput(bis, matrixX, 0, matrixX.length);
            JUtil.ReadInput(bis, matrixY, 0, matrixY.length);
            JUtil.ReadInput(bis, maskArray, 0, maskArray.length);
            JUtil.ReadInput(bis, formatInformationX2, 0, formatInformationX2.length);
            JUtil.ReadInput(bis, formatInformationY2, 0, formatInformationY2.length);
            JUtil.ReadInput(bis, rsEccCodewords, 0, rsEccCodewords.length);
            JUtil.ReadInput(bis, rsBlockOrderTemp, 0, rsBlockOrderTemp.length);

            bis.close();
            fis.close();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            throw new EncodingFailedException("Error reading a qrv file: " + e.getMessage());
        }
        byte rsBlockOrderLength = 1;
        for (byte i = 1; i <= 127; i++)
        {
            if (rsBlockOrderTemp[i] == 0)
            {
                    rsBlockOrderLength = i;
                    break;
            }
        }
        byte[] rsBlockOrder = new byte[rsBlockOrderLength];
        for (byte i = 0; i < rsBlockOrderLength; i++)
        {
            rsBlockOrder[i] = rsBlockOrderTemp[i];
        }
        byte[] formatInformationX1 = new byte[]{0, 1, 2, 3, 4, 5, 7, 8, 8, 8, 8, 8, 8, 8, 8};
        byte[] formatInformationY1 = new byte[]{8, 8, 8, 8, 8, 8, 8, 8, 7, 5, 4, 3, 2, 1, 0};
        int maxDataCodewords = maxDataBits >> 3;

        /* -- read frame data  -- */
        int modules1Side = 4 * qrcodeVersion + 17;
        int matrixTotalBits = modules1Side * modules1Side;
        byte[] frameData = new byte[matrixTotalBits + modules1Side];
        try
        {
            String fileName = "qrvfr" + qrcodeVersion + ".dat";
            File file;
            file = new File("/usr/local/forseti/rec/" + fileName);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            JUtil.ReadInput(bis, frameData, 0, frameData.length);
            bis.close();
            fis.close();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            throw new EncodingFailedException("Error reading a qrvfr file: " + e.getMessage());
        }

        // posicionar el terminador
        if (totalDataBits <= maxDataBits - 4)
        {
            dataValue[dataCounter] = 0;
            dataBits[dataCounter] = 4;
        }
        else
        {
            if (totalDataBits < maxDataBits)
            {
                dataValue[dataCounter] = 0;
                dataBits[dataCounter] = (byte) (maxDataBits - totalDataBits);
            }
            else
            {
                if (totalDataBits > maxDataBits)
                {
                        System.out.println("overflow");
                }
            }
        }
        byte[] dataCodewords = divideDataBy8Bits(dataValue, dataBits, maxDataCodewords);
        byte[] codewords = calculateRSECC(dataCodewords, rsEccCodewords[0], rsBlockOrder, maxDataCodewords, maxCodewords);

        // flasheo de Matriz
        byte[][] matrixContent = new byte[modules1Side][];
        for (int i2 = 0; i2 < modules1Side; i2++)
        {
            matrixContent[i2] = new byte[modules1Side];
        }
        for (int i = 0; i < modules1Side; i++)
        {
            for (int j = 0; j < modules1Side; j++)
            {
                    matrixContent[j][i] = 0;
            }
        }

        // Añadir datos
        for (int i = 0; i < maxCodewords; i++)
        {
            byte codeword_i = codewords[i];
            for (int j = 7; j >= 0; j--)
            {
                int codewordBitsNumber = (i * 8) + j;
                matrixContent[matrixX[codewordBitsNumber] & 0xFF][matrixY[codewordBitsNumber] & 0xFF] = (byte) ((255 * (codeword_i & 1)) ^ maskArray[codewordBitsNumber]);
                codeword_i = (byte) (JUtil.URShift((codeword_i & 0xFF), 1));
            }
        }
        for (int matrixRemain = matrixRemainBit[qrcodeVersion]; matrixRemain > 0; matrixRemain--)
        {
            int remainBitTemp = matrixRemain + (maxCodewords * 8) - 1;
            matrixContent[matrixX[remainBitTemp] & 0xFF][matrixY[remainBitTemp] & 0xFF] = (byte) (255 ^ maskArray[remainBitTemp]);
        }

        //Selección de máscara
        byte maskNumber = selectMask(matrixContent, matrixRemainBit[qrcodeVersion] + maxCodewords * 8);
        byte maskContent = (byte) (1 << maskNumber);

        // Información de formato
        byte formatInformationValue = (byte) (ec << 3 | maskNumber);
        String[] formatInformationArray = new String[]{"101010000010010", "101000100100101", "101111001111100", "101101101001011", "100010111111001", "100000011001110", "100111110010111", "100101010100000", "111011111000100", "111001011110011", "111110110101010", "111100010011101", "110011000101111", "110001100011000", "110110001000001", "110100101110110", "001011010001001", "001001110111110", "001110011100111", "001100111010000", "000011101100010", "000001001010101", "000110100001100", "000100000111011", "011010101011111", "011000001101000", "011111100110001", "011101000000110", "010010010110100", "010000110000011", "010111011011010", "010101111101101"};
        for (int i = 0; i < 15; i++)
        {
            byte content = (byte) Byte.parseByte(formatInformationArray[formatInformationValue].substring(i, i+1));

            matrixContent[formatInformationX1[i] & 0xFF][formatInformationY1[i] & 0xFF] = (byte) (content * 255);
            matrixContent[formatInformationX2[i] & 0xFF][formatInformationY2[i] & 0xFF] = (byte) (content * 255);
        }
        boolean[][] out_Renamed = new boolean[modules1Side][];
        for (int i3 = 0; i3 < modules1Side; i3++)
        {
            out_Renamed[i3] = new boolean[modules1Side];
        }
        int c = 0;
        for (int i = 0; i < modules1Side; i++)
        {
            for (int j = 0; j < modules1Side; j++)
            {
                if ((matrixContent[j][i] & maskContent) != 0 || frameData[c] == (char) 49)
                {
                        out_Renamed[j][i] = true;
                }
                else
                {
                        out_Renamed[j][i] = false;
                }
                c++;
            }
            c++;
        }
        return out_Renamed;
    }
	
    /**
     * Método privado que divide los datos en conjuntos de 8 bits para crear
     * los codewords, y añade el relleno (padding) sí es necesario.
     * @param data vector de int con los valores
     * @param bits vector de byte
     * @param maxDataCodewords nÂº máximo de codewords de datos
     * @return byte[] datos divididos en grupos de 8 bits
     * @throws EncodingFailedException
     */
    private static byte[] divideDataBy8Bits(int[] data, byte[] bits, int maxDataCodewords) throws EncodingFailedException
    {
        int l1 = bits.length;
        int l2;
        int codewordsCounter = 0;
        int remainingBits = 8;
        int max = 0;
        int buffer;
        int bufferBits;
        boolean flag;
        byte[] codewords;
        try
        {
            //if (l1 != data.length)
            //{
            //}
            for (int i = 0; i < l1; i++)
            {
                max += bits[i];
            }
            l2 = (max - 1) / 8 + 1;
            codewords = new byte[maxDataCodewords];
            for (int i = 0; i < l2; i++)
            {
                codewords[i] = 0;
            }
            for (int i = 0; i < l1; i++)
            {
                buffer = data[i];
                bufferBits = bits[i];
                flag = true;
                if (bufferBits == 0)
                {
                        break;
                }
                while (flag)
                {
                    if (remainingBits > bufferBits)
                    {
                        codewords[codewordsCounter] = (byte) ((codewords[codewordsCounter] << bufferBits) | buffer);
                        remainingBits -= bufferBits;
                        flag = false;
                    }
                    else
                    {
                        bufferBits -= remainingBits;
                        codewords[codewordsCounter] = (byte) ((codewords[codewordsCounter] << remainingBits) | (buffer >> bufferBits));

                        if (bufferBits == 0)
                        {
                                flag = false;
                        }
                        else
                        {
                                buffer = (buffer & ((1 << bufferBits) - 1));
                                flag = true;
                        }
                        codewordsCounter++;
                        remainingBits = 8;
                    }
                }
            }
        }
        catch(ArrayIndexOutOfBoundsException aexc)
        {
           throw new EncodingFailedException("ArrayIndexOutOfBoundsException throwed at divideDataBy8Bits");
        }
        if (remainingBits != 8)
        {
            codewords[codewordsCounter] = (byte) (codewords[codewordsCounter] << remainingBits);
        }
        else
        {
            codewordsCounter--;
        }
        if (codewordsCounter < maxDataCodewords - 1)
        {
            flag = true;
            while (codewordsCounter < maxDataCodewords - 1)
            {
                codewordsCounter++;
                if (flag)
                {
                        codewords[codewordsCounter] = - 20;
                }
                else
                {
                        codewords[codewordsCounter] = 17;
                }
                flag = !(flag);
            }
        }
        return codewords;
    }
	
    /**
     * Método privado que genera los codewords de corrección de error para
     * cada bloque del símbolo QRCode.
     * @param codewords byte[] con los codewords de datos ya calculados
     * @param rsEccCodewords byte número de codewords de ECC
     * @param rsBlockOrder byte[] con el orden de los bloques
     * @param maxDataCodewords int numero máximo de Codewords de datos para esta versión
     * @param maxCodewords int numero máximo de Codewords para esta versión
     * @return byte[] con los codewords de ECC
     */
    private static byte[] calculateRSECC(byte[] codewords, byte rsEccCodewords, byte[] rsBlockOrder, int maxDataCodewords, int maxCodewords)
    {
        byte[][] rsCalTableArray = new byte[256][];
        for (int i = 0; i < 256; i++)
        {
                rsCalTableArray[i] = new byte[rsEccCodewords];
        }
        try
        {
            String fileName = "rsc" + rsEccCodewords + ".dat";
            File file;
            file = new File("/usr/local/forseti/rec/"+ fileName);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            for (int i = 0; i < 256; i++)
            {
                    JUtil.ReadInput(bis, rsCalTableArray[i], 0, rsCalTableArray[i].length);
            }
            bis.close();
            fis.close();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            throw new EncodingFailedException("Error reading a rsc file: " + e.getMessage());
        }

        // Preparando ECC de Reed Solomon
        int i2 = 0;
        int j = 0;
        int rsBlockNumber = 0;
        byte[][] rsTemp = new byte[rsBlockOrder.length][];
        byte[] res = new byte[maxCodewords];
        for (int i = 0; i < codewords.length; i++)
            res[i] = codewords[i];
        if (maxCodewords < codewords.length)
            throw new EncodingFailedException("The number of codewords is higher than the maximum");

        i2 = 0;
        while (i2 < rsBlockOrder.length)
        {
            rsTemp[i2] = new byte[(rsBlockOrder[i2] & 0xFF) - rsEccCodewords];
            i2++;
        }
        i2 = 0;
        while (i2 < maxDataCodewords)
        {
            rsTemp[rsBlockNumber][j] = codewords[i2];
            j++;
            if (j >= (rsBlockOrder[rsBlockNumber] & 0xFF) - rsEccCodewords)
            {
                    j = 0;
                    rsBlockNumber++;
            }
            i2++;
        }

        // RS-ECC
        rsBlockNumber = 0;
        while (rsBlockNumber < rsBlockOrder.length)
        {
            byte[] rsTempData;
            //rsTempData = new byte[rsTemp[rsBlockNumber].length];
            rsTempData = (byte[])rsTemp[rsBlockNumber].clone();
            int rsCodewords = (rsBlockOrder[rsBlockNumber] & 0xFF);
            int rsDataCodewords = rsCodewords - rsEccCodewords;
            j = rsDataCodewords;
            while (j > 0)
            {
                byte first = rsTempData[0];
                if (first != 0)
                {
                    byte[] leftChr = new byte[rsTempData.length - 1];
                    for(int i = 0; i<rsTempData.length - 1; i++)
                            leftChr[i] = rsTempData[i + 1];
                    byte[] cal = rsCalTableArray[(first & 0xFF)];
                    rsTempData = calculateByteArrayBits(leftChr, cal, "xor");
                }
                else
                {
                    if (rsEccCodewords < rsTempData.length)
                    {
                        byte[] rsTempNew = new byte[rsTempData.length - 1];
                        for(int i = 0; i<rsTempData.length - 1; i++)
                                rsTempNew[i] = rsTempData[i + 1];
                        //rsTempData = new byte[rsTempNew.length];
                        rsTempData = (byte[])rsTempNew.clone();
                    }
                    else
                    {
                        byte[] rsTempNew = new byte[rsEccCodewords];
                        for(int i = 0; i<rsTempData.length - 1; i++)
                                rsTempNew[i] = rsTempData[i + 1];
                        rsTempNew[rsEccCodewords - 1] = 0;
                        //rsTempData = new byte[rsTempNew.length];
                        rsTempData = (byte[])rsTempNew.clone();
                    }
                }
                j--;
            }
            int aux = codewords.length + rsBlockNumber * rsEccCodewords;
            for(int i = aux; i < rsEccCodewords + aux; i++)
                    res[i] = rsTempData[i - aux];
            rsBlockNumber++;
        }
        return res;
    }

    /**
     * Método privado auxiliar para el cálculo de los ECC codewords.
     * @param xa byte[]
     * @param xb byte[]
     * @param ind String
     * @return byte[]
     */
    private static byte[] calculateByteArrayBits(byte[] xa, byte[] xb, String ind)
    {
        int ll;
        int ls;
        byte[] res;
        byte[] xl;
        byte[] xs;

        if (xa.length > xb.length)
        {
            xl = (byte[])xa.clone();
            xs = (byte[])xb.clone();
        }
        else
        {
            xl = (byte[])xb.clone();
            xs = (byte[])xa.clone();
        }
        ll = xl.length;
        ls = xs.length;
        res = new byte[ll];

        for (int i = 0; i < ll; i++)
        {
            if (i < ls)
            {
                if ((Object) ind == (Object) "xor")
                {
                    res[i] = (byte) (xl[i] ^ xs[i]);
                }
                else
                {
                    res[i] = (byte) (xl[i] | xs[i]);
                }
            }
            else
            {
                res[i] = xl[i];
            }
        }
        return res;
    }
	
    /**
     * Método privado para calcular la máscara de datos óptima.
     * @param matrixContent byte[][]
     * @param maxCodewordsBitWithRemain int máximo de bits con relleno para esta versión
     * @return byte que representa el tipo de máscara seleccionada
     */
    private static byte selectMask(byte[][] matrixContent, int maxCodewordsBitWithRemain)
    {
        int l = matrixContent.length;
        int[] d1 = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        int[] d2 = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        int[] d3 = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        int[] d4 = new int[]{0, 0, 0, 0, 0, 0, 0, 0};

        int d2And = 0;
        int d2Or = 0;
        int[] d4Counter = new int[]{0, 0, 0, 0, 0, 0, 0, 0};

        for (int y = 0; y < l; y++)
        {
                int[] xData = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
                int[] yData = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
                boolean[] xD1Flag = new boolean[]{false, false, false, false, false, false, false, false};
                boolean[] yD1Flag = new boolean[]{false, false, false, false, false, false, false, false};

                for (int x = 0; x < l; x++)
                {
                        if (x > 0 && y > 0)
                        {
                                d2And = matrixContent[x][y] & matrixContent[x - 1][y] & matrixContent[x][y - 1] & matrixContent[x - 1][y - 1] & 0xFF;
                                d2Or = (matrixContent[x][y] & 0xFF) | (matrixContent[x - 1][y] & 0xFF) | (matrixContent[x][y - 1] & 0xFF) | (matrixContent[x - 1][y - 1] & 0xFF);
                        }
                        for (int maskNumber = 0; maskNumber < 8; maskNumber++)
                        {
                                xData[maskNumber] = ((xData[maskNumber] & 63) << 1) | ((JUtil.URShift((matrixContent[x][y] & 0xFF), maskNumber)) & 1);
                                yData[maskNumber] = ((yData[maskNumber] & 63) << 1) | ((JUtil.URShift((matrixContent[y][x] & 0xFF), maskNumber)) & 1);

                                if ((matrixContent[x][y] & (1 << maskNumber)) != 0)
                                {
                                        d4Counter[maskNumber]++;
                                }
                                if (xData[maskNumber] == 93)
                                {
                                        d3[maskNumber] += 40;
                                }
                                if (yData[maskNumber] == 93)
                                {
                                        d3[maskNumber] += 40;
                                }
                                if (x > 0 && y > 0)
                                {
                                        if (((d2And & 1) != 0) || ((d2Or & 1) == 0))
                                        {
                                                d2[maskNumber] += 3;
                                        }
                                        d2And = d2And >> 1;
                                        d2Or = d2Or >> 1;
                                }
                                if (((xData[maskNumber] & 0x1F) == 0) || ((xData[maskNumber] & 0x1F) == 0x1F))
                                {
                                        if (x > 3)
                                        {
                                                if (xD1Flag[maskNumber])
                                                {
                                                        d1[maskNumber]++;
                                                }
                                                else
                                                {
                                                        d1[maskNumber] += 3;
                                                        xD1Flag[maskNumber] = true;
                                                }
                                        }
                                }
                                else
                                {
                                        xD1Flag[maskNumber] = false;
                                }
                                if (((yData[maskNumber] & 0x1F) == 0) || ((yData[maskNumber] & 0x1F) == 0x1F))
                                {
                                        if (x > 3)
                                        {
                                                if (yD1Flag[maskNumber])
                                                {
                                                        d1[maskNumber]++;
                                                }
                                                else
                                                {
                                                        d1[maskNumber] += 3;
                                                        yD1Flag[maskNumber] = true;
                                                }
                                        }
                                }
                                else
                                {
                                        yD1Flag[maskNumber] = false;
                                }
                        }
                }
        }
        int minValue = 0;
        byte res = 0;
        int[] d4Value = new int[]{90, 80, 70, 60, 50, 40, 30, 20, 10, 0, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 90};
        for (int maskNumber = 0; maskNumber < 8; maskNumber++)
        {
            d4[maskNumber] = d4Value[(int) ((20 * d4Counter[maskNumber]) / maxCodewordsBitWithRemain)];
            int demerit = d1[maskNumber] + d2[maskNumber] + d3[maskNumber] + d4[maskNumber];
            if (demerit < minValue || maskNumber == 0)
            {
                res = (byte) maskNumber;
                minValue = demerit;
            }
        }
        return res;
    }
	
    /**
     * Codifica una cadena según parámetros, en un símbolo QRCode.
     * @param cadena String cadena a codificar
     * @return BufferedImage imagen RGB con el símbolo QRcode
     */
    public BufferedImage Encode(String cadena)
    {
        boolean[][] matrix = calQrcode(cadena.getBytes());
        int size = (matrix.length * qrCodeScale) + (8 * qrCodeScale);//creamos la zona de silencio 4 modulos para cada borde

        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        g = g.create(0,0,(matrix.length * qrCodeScale) + (8 * qrCodeScale),
                (matrix.length * qrCodeScale) + (8 * qrCodeScale));
        g.setColor(qrCodeBackgroundColor);
        g.fillRect(0, 0, (matrix.length * qrCodeScale) + (8 * qrCodeScale), (matrix.length * qrCodeScale) + (8 * qrCodeScale));
        g.setColor(qrCodeForegroundColor);

        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix.length; j++)
            {
                if (matrix[j][i])
                {
                        g.fillRect((j * qrCodeScale) + (4 * qrCodeScale),
                            (i * qrCodeScale) + (4 * qrCodeScale), qrCodeScale, qrCodeScale);
                }
            }
        }
        return image;
    }
}