import React from "react";
import {Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from "@mui/material";
import Button from "@mui/material/Button";

export function DialogAlert(props) {
  const { confirmDialog, setConfirmDialog } = props;

  return (
    <div>
      <Dialog
        open={confirmDialog.isOpen}
        onClose={() =>
          setConfirmDialog({ ...confirmDialog, isOpen: false })
        }
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">{confirmDialog.subtitle}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            {confirmDialog.title}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={confirmDialog.onConfirm} color="primary" autoFocus>
            Ok
          </Button>
          <Button
            onClick={() =>
              setConfirmDialog({ ...confirmDialog, isOpen: false })
            }
            color="primary"
          >
            Cancelar
          </Button>

        </DialogActions>
      </Dialog>
    </div>
  );
}
