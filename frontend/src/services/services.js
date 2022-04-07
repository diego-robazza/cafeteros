import request from "./http";

export function getEntities() {
  return request({
    url: `/api/entes`,
  });
}

export function getInventory() {
  return request({
    url: `/api/inventories`,
  });
}

export function getAudit(id) {
  return request({
    url: `/api/inventories/${id}`,
  });
}

export function saveAudit({id, name, entity, date, status}) {
  return request({
    url: `/api/inventories/${id}`,
    method: "PUT",
    data: {
      id: id,
      nameAudit: name,
      auditStatus: status,
      startDate: date,
      ente: {
        activated: true,
        name: "",
        id: entity
      }
    },
  });
}

export function newAudit({name, entity, date, status}) {
  return request({
    url: `/api/inventories/`,
    method: "POST",
    data: {
      nameAudit: name,
      auditStatus: status,
      startDate: date,
      ente: {
        activated: true,
        name: "",
        id: entity
      }
    },
  });
}

export function deleteAudit(id) {
  return request({
    url: `/api/inventories/${id}`,
    method: "DELETE",
  });
}

export function getEntity(id) {
  return request({
    url: `/api/entes/${id}`,
  });
}

export function saveEntity({id, name, activated}) {
  return request({
    url: `/api/entes/${id}`,
    method: "PUT",
    data: {id, name, activated},
  });
}

export function newEntity({name, activated}) {
  return request({
    url: `/api/entes/`,
    method: "POST",
    data: {name, activated},
  });
}

export function deleteEntity(id) {
  return request({
    url: `/api/entes/${id}`,
    method: "DELETE",
  });
}

export function refreshToken(token) {
  return request({
    url: '/api/authenticate',
    method: "POST",
    data: {
      "password": "admin",
      "username": "admin",
      "rememberMe": true
    }
  });
}
