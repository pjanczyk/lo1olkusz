/*
 * Copyright 2016 Piotr Janczyk
 *
 * This file is part of lo1olkusz unofficial app.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pjanczyk.lo1olkusz.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Timetable of a specific class
 */
public final class Timetable implements Parcelable, Emptyable {

    private final String className;
    private final List<TimetableDay> days;

    public Timetable(@NonNull String className, @Nullable List<TimetableDay> days) {
        this.className = className;

        if (days == null) {
            this.days = Collections.emptyList();
        } else {
            this.days = new ArrayList<>(days); // defensive copy
        }
    }

    public boolean isEmpty() {
        return days.isEmpty();
    }

    @NonNull
    public String getClassName() {
        return className;
    }

    @Nullable
    public TimetableDay getDay(int day) {
        if (day >= 0 && day < days.size())
            return days.get(day);
        else
            return null;
    }

    @NonNull
    public List<TimetableDay> getAllDays() {
        return Collections.unmodifiableList(days);
    }

    /**
     * @return all existing groups in this timetable
     */
    @NonNull
    public Set<String> getAllGroups() {
        Set<String> groups = new TreeSet<>();

        for (TimetableDay d : days) {
            for (List<TimetableSubject> h : d.getHours().values()) {
                for (TimetableSubject s : h) {
                    String group = s.getGroup();
                    if (group != null) {
                        groups.add(group);
                    }
                }
            }
        }

        return groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Timetable timetable = (Timetable) o;

        return className.equals(timetable.className) && days.equals(timetable.days);
    }

    @Override
    public int hashCode() {
        int result = className.hashCode();
        result = 31 * result + days.hashCode();
        return result;
    }

    //parcelable part

    public Timetable(Parcel in) {
        className = in.readString();
        days = in.createTypedArrayList(TimetableDay.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(className);
        dest.writeTypedList(days);
    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Timetable createFromParcel(Parcel source) {
            return new Timetable(source);
        }

        @Override
        public Timetable[] newArray(int size) {
            return new Timetable[size];
        }
    };
}

