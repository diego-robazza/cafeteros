import axios from "axios";
import { API_URL } from "../../conf/env";
import { refreshToken } from "../services"

const http = axios.create({
  baseURL: API_URL,
});

function setRefreshToken(access_token) {
  sessionStorage.setItem('token', JSON.stringify(access_token));
}

export function getAccessToken() {
  return JSON.parse(sessionStorage.getItem('token'));
}

http.interceptors.request.use(
  (config) => {
    if ( !!getAccessToken())
      config.headers["Authorization"] = "Bearer " + getAccessToken();
    return config;
  },
  (error) => Promise.reject(error)
);

http.interceptors.response.use(
  (res) => res.data,
  async (error) => {
    if (error.response.status === 401) {
      let originalConfig = error.config;
      const refresh = await refreshToken();
      setRefreshToken(refresh.id_token);
      originalConfig.headers['Authorization'] = 'Bearer ' + refresh.id_token;
      return http(originalConfig);
    }
    return Promise.reject(error)
  }
);
export default http;
