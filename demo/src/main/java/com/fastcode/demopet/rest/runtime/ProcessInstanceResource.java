/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fastcode.demopet.rest.runtime;

import com.fastcode.demopet.model.runtime.FormModelRepresentation;
import com.fastcode.demopet.model.runtime.ProcessInstanceRepresentation;
import com.fastcode.demopet.service.runtime.FlowableProcessInstanceService;
import org.flowable.form.api.FormInfo;
import org.flowable.form.model.SimpleFormModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing a process instance.
 */
@RestController
@RequestMapping("/app")
public class ProcessInstanceResource {

    @Autowired
    protected FlowableProcessInstanceService processInstanceService;

    @RequestMapping(value = "/rest/process-instances/{processInstanceId}", method = RequestMethod.GET, produces = "application/json")
    public ProcessInstanceRepresentation getProcessInstance(@PathVariable String processInstanceId, HttpServletResponse response) {
        return processInstanceService.getProcessInstance(processInstanceId, response);
    }

    @RequestMapping(value = "/rest/process-instances/{processInstanceId}/start-form", method = RequestMethod.GET, produces = "application/json")
    public FormModelRepresentation getProcessInstanceStartForm(@PathVariable String processInstanceId, HttpServletResponse response) {
        FormInfo formInfo = processInstanceService.getProcessInstanceStartForm(processInstanceId, response);
        SimpleFormModel formModel = (SimpleFormModel) formInfo.getFormModel();
        return new FormModelRepresentation(formInfo, formModel);
    }

    @RequestMapping(value = "/rest/process-instances/{processInstanceId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteProcessInstance(@PathVariable String processInstanceId) {
        processInstanceService.deleteProcessInstance(processInstanceId);
    }

}
