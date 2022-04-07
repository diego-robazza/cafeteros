import React, {useEffect, useState} from "react";
import Grid from "@mui/material/Grid";
import Button from "@mui/material/Button";
import {useNavigate, useParams} from "react-router-dom";
import {ENTE} from "../navigation/CONSTANTS";
import {DialogAlert} from "../components/DialogAlert";
import {deleteEntity, getEntity, newEntity, saveEntity} from "../services/services"
import {Alert, Checkbox, FormControlLabel, FormGroup, Stack, TextField} from "@mui/material";
import Box from '@mui/material/Box';

export default function EnteEdit(props) {
  const {id} = useParams();
  const {data} = props;
  const [entity, setEntity] = useState({
    id: data ? data.id : null,
    name: data ? data.name : "",
    activated: data ? data.entity : false,
  });

  const [message, setMessage] = useState({text: ""});
  const [confirmDialog, setConfirmDialog] = useState({isOpen: false})
  const history = useNavigate();

  useEffect(() => {
    if (id) fetchEntity(id);
  }, [id]);

  const handleInputChange = (event) => {
    setEntity({
      ...entity,
      [event.target.name]: event.target.value,
    });
  };

  const handleCheckChange = (event) => {
    setEntity({
      ...entity,
      [event.target.name]: event.target.checked,
    });
  };

  const handleSave = () => {
    if (entity.name) {
      if (id) {
        saveEntity(entity)
          .then(() => {
            return history(ENTE, {state: {msg: `${entity.name} guardado exitosamente`}});
          })
          .catch();
      } else {
        newEntity(entity)
          .then(() => {
            return history(ENTE, {state: {msg: `${entity.name} creado exitosamente`}});
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
    deleteEntity(id)
      .then(() => {
        return history(ENTE, {state: {msg: "Borrado exitoso"}});
      })
      .catch((err) =>
        setMessage({
          text: err.response?.data?.detail,
          msgType: 'msgError'
        })
      );
  };

  function fetchEntity(id) {
    getEntity(id)
      .then((data) => {
        if (!data) return history(ENTE);
        setEntity(data)
      })
      .catch(error => {
        console.warn(error);
        history(ENTE);
      });
  }

  function handleBack() {
    history(ENTE);
  }

  return (
    <Grid container spacing={2}>
      <Grid item xs={10}>
        {message?.text && (
          <Alert severity="error">{message.text}</Alert>
        )}
      </Grid>
      <Grid item xs={5}><h2 style={{paddingLeft: 20}}>ENTIDAD DE CONTROL</h2></Grid>
      <Grid item xs={2}>
        {id && (
          <Button variant="contained" color="error" onClick={() => {
            setConfirmDialog({
              isOpen: true,
              title: "¿Desea eliminar este registro?",
              subtitle: "Eliminar",
              onConfirm: () => {
                handleDelete(entity.id)
              }
            })
          }}>Borrar</Button>
        )}
      </Grid>
      <Grid item xs={6}>
        <Box
          sx={{
            width: '150%',
            padding: 2
          }}
        >
          <form noValidate autoComplete="off">
            <div style={{marginBottom: 10}}>
              <TextField
                label="Titulo Entityoría"
                name="name"
                required
                variant="filled"
                size="small"
                fullWidth
                margin="normal"
                value={entity.name || ""}
                error={!entity.name}
                onChange={handleInputChange}
                InputLabelProps={{shrink: true}}
              />
            </div>
            <div style={{marginBottom: 10}}>
              <FormGroup>
                <FormControlLabel control={<Checkbox checked={entity.activated || false} onChange={handleCheckChange}
                                                     name="activated"/>} label="Activo"/>
              </FormGroup>
            </div>
            <Stack direction="row" spacing={2}>
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
