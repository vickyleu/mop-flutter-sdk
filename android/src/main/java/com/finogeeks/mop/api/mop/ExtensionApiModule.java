package com.finogeeks.mop.api.mop;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.finogeeks.lib.applet.client.FinAppClient;
import com.finogeeks.lib.applet.modules.log.FLog;
import com.finogeeks.mop.api.BaseApi;
import com.finogeeks.mop.interfaces.ICallback;
import com.finogeeks.mop.service.MopPluginService;
import com.finogeeks.mop.utils.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;


public class ExtensionApiModule extends BaseApi {

    private static final String TAG = "ExtensionApiModule";

    private Handler handler = new Handler(Looper.getMainLooper());

    public ExtensionApiModule(Context context) {
        super(context);
    }

    @Override
    public String[] apis() {
        return new String[]{"registerExtensionApi", "addWebExtentionApi"};
    }

    @Override
    public void invoke(String s, Map param, ICallback iCallback) {
        if (s.equals("registerExtensionApi")) {
            MethodChannel channel = MopPluginService.getInstance().getMethodChannel();
            String name = (String) param.get("name");
            Log.d(TAG, "registerExtensionApi:" + name);
            FinAppClient.INSTANCE.getExtensionApiManager().registerApi(new com.finogeeks.lib.applet.api.BaseApi(getContext()) {
                @Override
                public String[] apis() {
                    return new String[]{name};
                }

                @Override
                public void invoke(String s, JSONObject jsonObject, com.finogeeks.lib.applet.interfaces.ICallback iCallback) {
                    Log.d("MopPlugin", "invoke extensionApi:" + s + ",params:" + jsonObject);
                    Map params = GsonUtil.gson.fromJson(jsonObject.toString(), HashMap.class);
                    handler.post(() -> {
                        channel.invokeMethod("extensionApi:" + name, params, new MethodChannel.Result() {
                            @Override
                            public void success(Object result) {
                                String json = GsonUtil.gson.toJson(result);
                                FLog.d(ExtensionApiModule.TAG, "channel invokeMethod:" + name
                                        + " success, result=" + result + ", json=" + json);
                                JSONObject ret = null;
                                if (json != null && !json.equals("null")) {
                                    try {
                                        ret = new JSONObject(json);
                                        if (ret.has("errMsg")) {
                                            String errMsg = ret.getString("errMsg");
                                            if (errMsg.startsWith(name + ":fail")) {
                                                iCallback.onFail(ret);
                                                return;
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                iCallback.onSuccess(ret);
                            }

                            @Override
                            public void error(String errorCode, String errorMessage, Object errorDetails) {
                                FLog.e(ExtensionApiModule.TAG, "channel invokeMethod:" + name
                                        + " error, errorCode=" + errorCode
                                        + ", errorMessage=" + errorMessage
                                        + ", errorDetails=" + errorDetails);
                                iCallback.onFail();
                            }

                            @Override
                            public void notImplemented() {
                                iCallback.onFail();
                            }
                        });
                    });
                }
            });
        } else if (s.equals("addWebExtentionApi")) {
            MethodChannel channel = MopPluginService.getInstance().getMethodChannel();
            String name = (String) param.get("name");
            Log.d(TAG, "addWebExtentionApi:" + name);
            FinAppClient.INSTANCE.getExtensionWebApiManager().registerApi(new com.finogeeks.lib.applet.api.BaseApi(getContext()) {
                @Override
                public String[] apis() {
                    return new String[]{name};
                }

                @Override
                public void invoke(String s, JSONObject jsonObject, com.finogeeks.lib.applet.interfaces.ICallback iCallback) {
                    Log.d("MopPlugin", "invoke webextensionApi:" + s + ",params:" + jsonObject);
                    Map params = GsonUtil.gson.fromJson(jsonObject.toString(), HashMap.class);
                    handler.post(() -> {
                        channel.invokeMethod("webExtentionApi:" + name, params, new MethodChannel.Result() {
                            @Override
                            public void success(Object result) {
                                String json = GsonUtil.gson.toJson(result);
                                FLog.d(ExtensionApiModule.TAG, "channel invokeMethod:" + name
                                        + " success, result=" + result + ", json=" + json);
                                JSONObject ret = null;
                                if (json != null && !json.equals("null")) {
                                    try {
                                        ret = new JSONObject(json);
                                        if (ret.has("errMsg")) {
                                            String errMsg = ret.getString("errMsg");
                                            if (errMsg.startsWith(name + ":fail")) {
                                                iCallback.onFail(ret);
                                                return;
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                iCallback.onSuccess(ret);
                            }

                            @Override
                            public void error(String errorCode, String errorMessage, Object errorDetails) {
                                FLog.e(ExtensionApiModule.TAG, "channel invokeMethod:" + name
                                        + " error, errorCode=" + errorCode
                                        + ", errorMessage=" + errorMessage
                                        + ", errorDetails=" + errorDetails);
                                iCallback.onFail();
                            }

                            @Override
                            public void notImplemented() {
                                iCallback.onFail();
                            }
                        });
                    });
                }
            });
        }

    }
}
