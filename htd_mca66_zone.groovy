/**
 *  HTD MCA66 Amplifier Zone
 *
 *  Copyright 2020 Jeff Mehlman
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.

 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.

 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *   Change Log:
 *     12/21/2020 v1.0 - Initial release
 *
 */

metadata {
    definition(name: "HTD MCA66 Amplifier Zone", namespace: "htdmca66", author: "Jeff Mehlman") {
        command "sendTestMessage"
        command "selectInput", [[name:"inputNum",type:"NUMBER", description:"Input Number", constraints:["NUMBER"]]]

        capability "AudioVolume"
        capability "HealthCheck"
        capability "Switch"

        attribute "ZoneNumber", "number"
        attribute "ZoneName", "string"
    }

    preferences{
        input name: 'zoneName', type: 'text', title: 'Zone Name', required: true, defaultValue: 'Zone X', description: 'Name for Zone'
    }
}

void configure() {}

void installed() {
    runIn(1, "off")
}

void updated() {
    log.debug "${zoneName}"
    sendEvent(name: 'ZoneName',value: zoneName)
}

void setZone(zone) {
    state.ZoneNumber = zone
}

int getZone() {
    return state.ZoneNumber
}

void volumeUp() {
    def zone = state.ZoneNumber as int

    getParent().volumeUp(zone)
}

void volumeDown() {
    def zone = state.ZoneNumber as int

    getParent().volumeDown(zone)
}


void setVolume(volume) {
    if (device.currentValue('switch') == 'off') {
        log.debug "Device off, no volume control"
        return
    }

    def zone = state.ZoneNumber as byte

    def currentVolume = device.currentValue('volume')*60/100 as int

    def desiredVolume = volume*60/100 as int

    log.debug "Input Volume: ${volume}, Desired Volume: ${desiredVolume}, Current Volume: ${currentVolume}"

    state.updatingVolume = true

    if (currentVolume < desiredVolume)
    {
        def diff = desiredVolume - currentVolume
        for (i in 1..diff) {
            volumeUp()
        }
        state.updatingVolume = false

    }
    else if (currentVolume > desiredVolume )
    {
        def diff = currentVolume - desiredVolume
        for (i in 1..diff) {
            volumeDown()
        }
        state.updatingVolume = false
    }
    else
    {
        state.updatingVolume = false
        return
    }
}

void setLevel(volume) {
    setVolume(volume)
}

void on() {
    def zone = state.ZoneNumber as byte

    getParent().on(zone)
    //sendEvent(name: "switch", value: "on")
}

void off() {
    def zone = state.ZoneNumber as byte

    getParent().off(zone)
    //sendEvent(name: "switch", value: "off")
}

void mute() {
    def zone = state.ZoneNumber as byte

    if (state.mute == 'unmuted') {
        getParent().toggleMute(zone)
    }
}

void unmute() {
    def zone = state.ZoneNumber as byte

    if (state.mute == 'muted') {
        getParent().toggleMute(zone)
    }
}

void selectInput(inputNum) {
    def zone = state.ZoneNumber as byte

    getParent().selectInput(zone, inputNum as byte)
}

void updateState(statesMap) {
    if (state.updatingVolume == null)
    {
        state.updatingVolume = false
    }

    if (state.updatingVolume == false) {
        statesMap.each{entry -> sendEvent(name: entry.key, value: entry.value)
        state."${entry.key}" = entry.value
        }
    }
}
