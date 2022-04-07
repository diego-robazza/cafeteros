import React, {useEffect, useState} from "react";
import Grid from "@mui/material/Grid";
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import Button from "@mui/material/Button";
import {useNavigate, useParams} from "react-router-dom";
import {ROOT} from "../navigation/CONSTANTS";
import {DialogAlert} from "../components/DialogAlert";
import moment from "moment";
import {deleteAudit, getAudit, getEntities, newAudit, saveAudit} from "../services/services"
import {Alert, Stack, TextField} from "@mui/material";
import Box from '@mui/material/Box';

export default function AuditEdit(props) {
  const {id} = useParams();
  const {data} = props;
  const [audit, setAudit] = useState({
    id: data ? data.id : null,
    name: data ? data.name : "",
    entity: data ? data.entity : "",
    date: data ? data.date : "",
    status: data ? data.status : "PROCESO",
  });
  const [entities, setEntities] = useState([]);
  const [message, setMessage] = useState({text: ""});
  const [confirmDialog, setConfirmDialog] = useState({isOpen: false})
  const history = useNavigate();

  useEffect(() => {
    if (id) fetchAudit(id);
  }, [id]);

  useEffect(() => {
    async function fetchData() {
      setEntities(await getEntities());
    }

    fetchData();
  }, []);

  const handleInputChange = (event) => {
    setAudit({
      ...audit,
      [event.target.name]: event.target.value,
    });
  };

  const handleSave = () => {
    if (audit.name && audit.entity) {
      let dataSave = {...audit, date: moment(audit.startDate)};
      if (id) {
        saveAudit(dataSave)
          .then(() => {
            return history(ROOT, {state: {msg: `${dataSave.name} guardado exitosamente`}});
          })
          .catch();
      } else {
        newAudit(dataSave)
          .then(() => {
            return history(ROOT, {state: {msg: `${dataSave.name} creado exitosamente`}});
          })
          .catch();
      }
    } else {
      setMessage({text: "Complete todos los datos por favor"})
    }
  };

  const handleDelete = (id) => {
    setConfirmDialog({
      ...confirmDialog,
      isOpen: false
    })

    if (!id) {
      return;
    }
    deleteAudit(id)
      .then(() => {
        return history(ROOT, {state: {msg: "Borrado exitoso"}});
      })
      .catch((err) =>
        setMessage({
          text: err.response?.data?.message,
          msgType: 'msgError'
        })
      );
  };

  function fetchAudit(id) {
    getAudit(id)
      .then((data) => {
        if (!data) return history(ROOT);
        setAudit({
          id: data.id,
          name: data.nameAudit,
          entity: data.ente.id,
          date: moment(data.startDate).format("YYYY-MM-DD"),
          status: data.auditStatus,
        })
      })
      .catch(error => {
        console.warn(error);
        history(ROOT);
      });
  }

  function handleBack() {
    history(ROOT);
  }

  return (
    <Grid container spacing={2}>
      <Grid item xs={10}>
        {message?.text && (
          <Alert severity="error">{message.text}</Alert>
        )}
      </Grid>
      <Grid item xs={5}><h2 style={{paddingLeft: 20}}>Auditoría</h2></Grid>
      <Grid item xs={2}>
        {id && (
          <Button variant="contained" color="error" onClick={() => {
            setConfirmDialog({
              isOpen: true,
              title: "¿Desea eliminar este registro?",
              subtitle: "Eliminar",
              onConfirm: () => {
                handleDelete(audit.id)
              }
            })
          }}>Borrar</Button>
        )}
      </Grid>
      <Grid item xs={6}>
        <Box
          sx={{
            width:'150%',
            padding: 2
          }}
        >
        <form noValidate autoComplete="off">
          <div style={{marginBottom: 10}}>
            <TextField
              label="Titulo Auditoría"
              name="name"
              required
              variant="filled"
              size="small"
              fullWidth
              margin="normal"
              value={audit.name || ""}
              error={!audit.name}
              onChange={handleInputChange}
              InputLabelProps={{shrink: true}}
            />
          </div>
          <div style={{marginBottom: 10}}>
            <FormControl fullWidth>
              <InputLabel id="select-entity-label">Ente de control</InputLabel>
              <Select
                labelId="select-entity-label"
                id="select-entity"
                value={audit.entity}
                label="Ente de control"
                name="entity"
                onChange={handleInputChange}
              >
                {entities && entities.map((ent) => {
                  return <MenuItem key={ent.id} value={ent.id}>{ent.name}</MenuItem>
                })
                }
              </Select>
            </FormControl>
          </div>
          <div style={{marginBottom: 20}}>
            <TextField
              label="Fecha"
              name="date"
              required
              variant="filled"
              size="small"
              fullWidth
              type="date"
              margin="normal"
              value={audit.date || moment().format("YYYY-MM-DD")}
              error={!audit.date}
              onChange={handleInputChange}
              InputLabelProps={{shrink: true}}
            />
          </div>
          <div style={{marginBottom: 10}}>
            <FormControl fullWidth>
              <InputLabel id="select-status-label">Estado</InputLabel>
              <Select
                labelId="select-status-label"
                id="select-status"
                value={audit.status}
                label="Estado"
                name="status"
                onChange={handleInputChange}
              >
                <MenuItem value="PROCESO">Proceso</MenuItem>
                <MenuItem value="FINALIZADA">Finalizada</MenuItem>
              </Select>
            </FormControl>
          </div>
          <Stack
            direction="row"
            spacing={2}
            sx={{
              marginTop:'10px'
            }}
          >
            <Button variant="contained" onClick={handleSave}>Guardar</Button>
            <Button variant="contained" onClick={handleBack} color="secondary">Cancelar</Button>
          </Stack>
        </form>
        </Box>
      </Grid>

      <DialogAlert
        confirmDialog={confirmDialog}
        setConfirmDialog={setConfirmDialog}
      />
    </Grid>
  );
}
