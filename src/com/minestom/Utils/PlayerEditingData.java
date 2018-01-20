package com.minestom.Utils;

import java.util.HashMap;
import java.util.Map;

public class PlayerEditingData {

    private Map<String, String> barValues = new HashMap<>();
    private String barKeyName;
    private boolean editing;
    private boolean editTimer;
    private boolean editingName;
    private boolean createBar;
    private boolean confirm;
    private boolean saving;
    private boolean canceling;
    private boolean deleting;
    private boolean addingCmd;
    private boolean announcerTime;
    private boolean editPeriod;

    public PlayerEditingData() {
        this.editing = false;
        this.editTimer = false;
        this.editingName = false;
        this.createBar = false;
        this.confirm = false;
        this.saving = false;
        this.canceling = false;
        this.deleting = false;
        this.addingCmd = false;
        this.announcerTime = false;
        this.editPeriod = false;
    }

    public String getBarValue(String value) {
        return barValues.get(value);
    }

    public void addBarValue(String value, String value2) {
        barValues.put(value, value2);
    }

    public String getBarKeyName() {
        return barKeyName;
    }

    public void setBarKeyName(String barKeyName) {
        this.barKeyName = barKeyName;
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public boolean isEditTimer() {
        return editTimer;
    }

    public void setEditTimer(boolean editTimer) {
        this.editTimer = editTimer;
    }

    public boolean isEditingName() {
        return editingName;
    }

    public void setEditingName(boolean editingName) {
        this.editingName = editingName;
    }

    public boolean isCreateBar() {
        return createBar;
    }

    public void setCreateBar(boolean createBar) {
        this.createBar = createBar;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public boolean isSaving() {
        return saving;
    }

    public void setSaving(boolean saving) {
        this.saving = saving;
    }

    public boolean isCanceling() {
        return canceling;
    }

    public void setCanceling(boolean canceling) {
        this.canceling = canceling;
    }

    public boolean isDeleting() {
        return deleting;
    }

    public void setDeleting(boolean deleting) {
        this.deleting = deleting;
    }

    public boolean isAddingCmd() {
        return addingCmd;
    }

    public void setAddingCmd(boolean addingCmd) {
        this.addingCmd = addingCmd;
    }

    public boolean isAnnouncerTime() {
        return announcerTime;
    }

    public void setAnnouncerTime(boolean announcerTime) {
        this.announcerTime = announcerTime;
    }

    public boolean isEditPeriod() {
        return editPeriod;
    }

    public void setEditPeriod(boolean editPeriod) {
        this.editPeriod = editPeriod;
    }
}
