import React from "react";
import { Routes, Route } from "react-router-dom";
import {
  ROOT,
  AUDITORIA_EDIT,
  ENTE,
  ENTE_EDIT
} from "./CONSTANTS";
import Home from "../pages/Home"
import AuditEdit from "../pages/AuditEdit";
import Ente from "../pages/Ente";
import EnteEdit from "../pages/EnteEdit";

export const RouterConfig = () => {
  return (
    <Routes>
      <Route path={ROOT} element={<Home/>}/>
      <Route path={`${AUDITORIA_EDIT}`}>
        <Route path="" element={<AuditEdit/>}/>
        <Route path=":id" element={<AuditEdit/>}/>
      </Route>
      <Route path={ENTE} element={<Ente/>}/>
      <Route path={`${ENTE_EDIT}`}>
        <Route path="" element={<EnteEdit/>}/>
        <Route path=":id" element={<EnteEdit/>}/>
      </Route>
    </Routes>
  );
};
