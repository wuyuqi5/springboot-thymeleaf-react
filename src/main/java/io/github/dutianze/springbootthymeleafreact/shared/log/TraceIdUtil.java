package io.github.dutianze.springbootthymeleafreact.shared.log;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.StringUtils;

public class TraceIdUtil {

  private static final AtomicInteger COUNT = new AtomicInteger(1000);
  private static String IP_16 = "ffffffff";
  private static String P_ID_CACHE = null;

  static {
    try {
      String ipAddress = getInetAddress();
      if (ipAddress != null) {
        IP_16 = getIp16(ipAddress);
      }
    } catch (Throwable e) {
      /*
       * empty catch block
       */
    }
  }

  public static String generate() {
    return getTraceId(IP_16, System.currentTimeMillis(), getNextId());
  }

  private static String getTraceId(String ip, long timestamp, int nextId) {
    return ip + timestamp + nextId + getPid();
  }

  private static String getIp16(String ip) {
    String[] ips = ip.split("\\.");
    StringBuilder sb = new StringBuilder();
    for (String column : ips) {
      String hex = Integer.toHexString(Integer.parseInt(column));
      if (hex.length() == 1) {
        sb.append('0').append(hex);
      } else {
        sb.append(hex);
      }

    }
    return sb.toString();
  }

  private static String getInetAddress() {
    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      InetAddress address;
      while (interfaces.hasMoreElements()) {
        NetworkInterface ni = interfaces.nextElement();
        Enumeration<InetAddress> addresses = ni.getInetAddresses();
        while (addresses.hasMoreElements()) {
          address = addresses.nextElement();
          if (!address.isLoopbackAddress() && !address.getHostAddress().contains(":")) {
            return address.getHostAddress();
          }
        }
      }
      return null;
    } catch (Throwable t) {
      return null;
    }
  }

  private static int getNextId() {
    for (; ; ) {
      int current = COUNT.get();
      int next = (current > 9000) ? 1000 : current + 1;
      if (COUNT.compareAndSet(current, next)) {
        return next;
      }
    }
  }

  private static String getPid() {
    if (P_ID_CACHE != null) {
      return P_ID_CACHE;
    }
    String processName = ManagementFactory.getRuntimeMXBean().getName();

    if (StringUtils.isBlank(processName)) {
      return "";
    }

    String[] processSplitName = processName.split("@");

    if (processSplitName.length == 0) {
      return "";
    }

    String pid = processSplitName[0];

    if (StringUtils.isBlank(pid)) {
      return "";
    }
    P_ID_CACHE = pid;
    return pid;
  }
}
