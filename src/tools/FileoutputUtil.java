package tools;

import client.MapleCharacter;
import java.io.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileoutputUtil {

    private static final SimpleDateFormat sdfT = new SimpleDateFormat("yyyyÄêMMÔÂddÈÕHHÊ±mm·ÖssÃë");
    // Logging output file
    public static final String fixdam_mg = "log\\Ä§·¨ÉËº¦ÐÞÕý.log",
            fixdam_ph = "log\\ÎïÀíÉËº¦ÐÞÕý.log",
            MobVac_log = "log\\Log_Îü¹Ö.log",
            hack_log = "log\\Log_»³ÒÉÍâ¹Ò.log",
            ban_log = "log\\Log_·âºÅ.log",
            Acc_Stuck = "log\\Log_¿¨ÕËºÅ.log",
            Login_Error = "log\\Log_µÇÂ¼´íÎó.log",
            //Timer_Log = "log\\Log_Timer_Except.log",
            //MapTimer_Log = "log\\Log_MapTimer_Except.log",
            IP_Log = "log\\Log_ÕËºÅIP.log",
            //GMCommand_Log = "Log_GMCommand.log",
            Zakum_Log = "log\\Log_ÔúÀ¥.log",
            Horntail_Log = "log\\Log_°µºÚÁúÍõ.log",
            Pinkbean_Log = "log\\Log_Æ·¿ËçÍ.log",
            ScriptEx_Log = "log\\Log_Script_½Å±¾Òì³£.log",
            PacketEx_Log = "log\\Log_Packet_·â°üÒì³£.log" // I cba looking for every error, adding this back in.
            + "";
    // End
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdf_ = new SimpleDateFormat("yyyy-MM-dd");

    public static void logToFile_chr(MapleCharacter chr, final String file, final String msg) {
        logToFile(file, "\r\n" + FileoutputUtil.CurrentReadable_Time() + " ÕËºÅ£º" + chr.getClient().getAccountName() + " ½ÇÉ«£º" + chr.getName() + " (" + chr.getId() + ") µÈ¼¶£º" + chr.getLevel() + " µØÍ¼£º" + chr.getMapId() + " " + msg, false);
    }

    public static void logToFile(final String file, final String msg) {
        logToFile(file, msg, false);
    }

    public static void logToFile(final String file, final String msg, boolean notExists) {
        FileOutputStream out = null;
        try {
            File outputFile = new File(file);
            if (outputFile.exists() && outputFile.isFile() && outputFile.length() >= 10 * 1024 * 1000) {
                outputFile.renameTo(new File(file.substring(0, file.length() - 4) + "_" + sdfT.format(Calendar.getInstance().getTime()) + file.substring(file.length() - 4, file.length())));
                outputFile = new File(file);
            }
            if (outputFile.getParentFile() != null) {
                outputFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(file, true);
            if (!out.toString().contains(msg) || !notExists) {
                OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
                osw.write(msg);
                osw.flush();
            }
        } catch (IOException ess) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    public static void packetLog(String file, String msg) {
        boolean notExists = false;
        FileOutputStream out = null;
        try {
            File outputFile = new File(file);
            if (outputFile.exists() && outputFile.isFile() && outputFile.length() >= 1024 * 1000) {
                outputFile.renameTo(new File(file.substring(0, file.length() - 4) + "_" + sdfT.format(Calendar.getInstance().getTime()) + file.substring(file.length() - 4, file.length())));
                outputFile = new File(file);
            }
            if (outputFile.getParentFile() != null) {
                outputFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(file, true);
            if (!out.toString().contains(msg) || !notExists) {
                OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
                osw.write(msg);
                osw.flush();
            }
        } catch (IOException ess) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    public static void log(final String file, final String msg) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file, true);
            out.write(("\n------------------------ " + CurrentReadable_Time() + " ------------------------\n").getBytes());
            out.write(msg.getBytes());
        } catch (IOException ess) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    public static void outputFileError(final String file, final Throwable t) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file, true);
            out.write(("\n------------------------ " + CurrentReadable_Time() + " ------------------------\n").getBytes());
            out.write(getString(t).getBytes());
        } catch (IOException ess) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    public static String CurrentReadable_Date() {
        return sdf_.format(Calendar.getInstance().getTime());
    }

    public static String CurrentReadable_Time() {
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static String getString(final Throwable e) {
        String retValue = null;
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            retValue = sw.toString();
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
                if (sw != null) {
                    sw.close();
                }
            } catch (IOException ignore) {
            }
        }
        return retValue;
    }

    public static String NowTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
