/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package scripting;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import client.MapleClient;
import java.io.*;
import java.util.stream.Collectors;
import tools.FileoutputUtil;

/**
 *
 * @author Matze
 */
public abstract class AbstractScriptManager {

    private static final ScriptEngineManager sem = new ScriptEngineManager();

    protected Invocable getInvocable(String path, MapleClient c) {
        return getInvocable(path, c, false);
    }

    protected Invocable getInvocable(String path, MapleClient c, boolean npc) {
        InputStream in = null;
        try {
            path = "scripts/" + path;
            ScriptEngine engine = null;

            if (c != null) {
                engine = c.getScriptEngine(path);
            }
            
            if (engine == null) {
                File scriptFile = new File(path);
                if (!scriptFile.exists()) {
                    return null;
                }
                
                engine = sem.getEngineByName("nashorn");

                if (c != null) {
                    c.setScriptEngine(path, engine);
                }

                in = new FileInputStream(scriptFile);
                BufferedReader bf = new BufferedReader(new InputStreamReader(in, EncodingDetect.getJavaEncode(scriptFile)));
                String lines = "load('nashorn:mozilla_compat.js');" + bf.lines().collect(Collectors.joining(System.lineSeparator()));
                engine.eval(lines);
            } else if (c != null && npc) {
                c.getPlayer().dropMessage(5, "[系统提示] 角色可能进入假死状态（不能使用技能，不能打开菜单），请使用 @k 命令解除异常。");
            }
            
            return (Invocable) engine;
        } catch (Exception e) {
            System.out.printf("脚本执行出错，脚本文件：" + path + "\r\n异常详情：" + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "脚本执行出错，脚本文件：" + path + "\r\n异常详情：" + e);
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ignore) {
            }
        }
    }
}
