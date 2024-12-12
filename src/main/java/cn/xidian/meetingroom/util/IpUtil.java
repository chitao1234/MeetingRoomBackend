package cn.xidian.meetingroom.util;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtil {
    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR" };

    public static String getClientIpAddress(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }

        return request.getRemoteAddr();
    }

    public static byte[] getIpAddressBytes(HttpServletRequest request) {
        String ip = getClientIpAddress(request);
        
        // Check if IPv6
        if (ip.contains(":")) {
            try {
                java.net.InetAddress addr = java.net.InetAddress.getByName(ip);
                if (addr instanceof java.net.Inet6Address) {
                    return addr.getAddress(); // Returns 16 bytes for IPv6
                }
                throw new IllegalArgumentException("Expected IPv6 address: " + ip);
            } catch (java.net.UnknownHostException e) {
                throw new IllegalArgumentException("Invalid IPv6 address: " + ip, e);
            }
        }
        
        // Handle IPv4
        try {
            java.net.Inet4Address inet4Address = (java.net.Inet4Address) java.net.InetAddress.getByName(ip);
            return inet4Address.getAddress(); // Returns 4 bytes for IPv4
        } catch (java.net.UnknownHostException e) {
            throw new IllegalArgumentException("Invalid IPv4 address: " + ip, e);
        }
    }

}