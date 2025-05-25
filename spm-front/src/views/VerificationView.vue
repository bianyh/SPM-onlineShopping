<template>
    <div class="container">
        <h2>Applications</h2>
        <el-container class="subcontainer">
            <el-aside width="300px" class="left-list">
                <el-select v-model="filterType" placeholder="Filter by Type">
                    <el-option label="All" value=""></el-option>
                    <el-option label="Type 1" value="type1"></el-option>
                    <el-option label="Type 2" value="type2"></el-option>
                </el-select>
                <el-card v-for="(app, index) in filteredApplications" :key="index" class="app-card" shadow="hover"
                    @click="selectApplication(app)"
                    :class="{ selected: selectedApplication && app.id === selectedApplication.id }">
                    <div>
                        <p><strong>Application Name:</strong> {{ app.name }}</p>
                        <p><strong>Application Type:</strong> {{ app.type }}</p>
                    </div>
                </el-card>
            </el-aside>
            <el-main class="right-panel">
                <el-card v-if="selectedApplication" shadow="never">
                    <div>
                        <p><strong>Applicant:</strong> {{ selectedApplication.applicant }}</p>
                        <p><strong>Application Type:</strong> {{ selectedApplication.type }}</p>
                        <p><strong>Application Content:</strong> {{ selectedApplication.content }}</p>
                    </div>
                    <el-input v-model="feedback" type="textarea" placeholder="Enter your feedback"
                        class="feedback-input"></el-input>
                    <div class="button-group">
                        <el-button @click="approve" type="success">Approve</el-button>
                        <el-button @click="reject" type="danger">Reject</el-button>
                    </div>
                </el-card>
            </el-main>
        </el-container>
    </div>
</template>

<script>
export default {
    data() {
        return {
            filterType: '',
            applications: [
                {
                    id: 1,
                    name: 'App 1',
                    type: 'type1',
                    applicant: 'John Doe',
                    content: 'This is the content of application 1.'
                },
                {
                    id: 2,
                    name: 'App 2',
                    type: 'type2',
                    applicant: 'Jane Smith',
                    content: 'This is the content of application 2.'
                }
            ],
            selectedApplication: null,
            feedback: ''
        };
    },
    computed: {
        filteredApplications() {
            if (!this.filterType) {
                return this.applications;
            }
            return this.applications.filter(app => app.type === this.filterType);
        }
    },
    methods: {
        selectApplication(app) {
            this.selectedApplication = app;
        },
        approve() {
            console.log('Approve application:', this.selectedApplication.id);
        },
        reject() {
            console.log('Reject application:', this.selectedApplication.id);
        }
    }
};
</script>

<style scoped>
.app-card {
    margin-bottom: 15px;
    cursor: pointer;
    padding: 15px;
}

.selected {
    border-color: #409EFF;
}

.feedback-input {
    margin-top: 15px;
    margin-bottom: 15px;
}

.button-group {
    display: flex;
    justify-content: space-between;
}

.container {
    width: 100%;
    height: 100dvh;
    padding-top: 3.5rem;
}
.subcontainer {
    height: calc(100% - 2rem);
}

h2 {
    width: 100%;
    background-color: #fff;
    text-align: left;
    padding-left: 2rem;
    line-height: 2rem;
}

.left-list {
    left: 0;
    background-color: #fff;
    padding: 1rem;
    height: 100%;
    overflow: clip scroll;
}

.right-panel {
    box-shadow: 0px 0px 10px #00000080;
    background-color: #ffffff40;
    backdrop-filter: blur(10px);
}
</style>