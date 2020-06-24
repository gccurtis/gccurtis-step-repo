// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class FindMeetingQuery {

	public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
		List<TimeRange> times = new ArrayList<>();
		times.add(TimeRange.WHOLE_DAY);
		List<TimeRange> newtimes = new ArrayList<>();
		if (request.getAttendees().size() == 0){
			return Arrays.asList(TimeRange.WHOLE_DAY);
		}
		if (request.getDuration() > TimeRange.WHOLE_DAY.duration()){
			return Arrays.asList();
		}
		for (Event e: events){
			if(request.getAttendees().containsAll(e.getAttendees())){
				TimeRange etime = e.getWhen();
				for (TimeRange time: times){
					if (time.contains(etime)){
						newtimes.add(TimeRange.fromStartEnd(time.start(),etime.start(),false));
						newtimes.add(TimeRange.fromStartEnd(etime.end(),time.end(),false));
					}
					else if (time.contains(etime.start())){
						newtimes.add(TimeRange.fromStartEnd(time.start(),etime.start(),false));
					}
					else if (time.contains(etime.end())){
						newtimes.add(TimeRange.fromStartEnd(etime.end(),time.end(),false));
					}
					else{
						newtimes.add(time);
					}
				}
				times = new ArrayList<>();
				for(TimeRange newtime: newtimes){
					if(!(newtime.start() == newtime.end()) && !(newtime.duration() < request.getDuration())){
						times.add(newtime);
					}
				}
				newtimes = new ArrayList<>();
			}

		}
		return times; 
	}
}
