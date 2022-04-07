import http from "./interceptor";

const request = ({
   url = "",
   method = "GET",
   headers = {},
   params = {},
   data = {},
 }) => {
  const comHeaders = {
    Accept: "application/json",
    "Content-Type": "application/json",
    //"Authorization": "Bearer 1eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTY1MTg2Mzc3NX0.qt2KLR9r_RA-nqCH-Blgea1dfpsg5c5nOmG0qo9L9OgeuMvij9cPqp0lt1JuNP_bFkYwK5nkK3Qy3v3qZx6Pig",
    ...headers,
  };
  switch (method) {
    case "GET":
      return http.get(url, { headers: comHeaders, params, data });
    case "POST":
      return http.post(url, data, { headers: comHeaders, params });
    case "PUT":
      return http.put(url, data, { headers: comHeaders, params });
    case "DELETE":
      return http.delete(url, { headers: comHeaders, params, data });
    default:
      break;
  }
};

export default request;
