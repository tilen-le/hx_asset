export default {
  label_width: '125px',
  label_medium_width: '145px',
  label_large_width: '156px',
  //系统
  project: {
    name: 'ASSET MANAGER',
    logo: 'ASSET MANAGER',
  },
  //公用
  common_field: {
    add: 'Add',
    update: 'Edit',
    delete: 'Delete',
    search: 'Search',
    reset: 'Reset',
    import: 'Import',
    export: 'Export',
    download_template: 'Download Template',
    refresh: 'Refresh',
    refresh_cache: 'Refresh Caches',
    clear: 'Clear',
    show_column: 'Show/hide Column',
    show_search: 'Hide Search',
    expand_collapse: 'Expand/Fold',
    operate: 'Operate',
    more: 'More',
    detail: 'Details',
    confirm: 'Confirm',
    cancel: 'Cancel',
    submit: 'Submit',
    back: 'Back',
    close: 'Close',
    save: 'Save',
    not_null: 'This is required',
    status: 'Status',
    type: 'Type',
    create_time: 'Create Time',
    update_time: 'Update Time',
    create_by: 'Creator',
    start_date: 'Start Date',
    end_date: 'End Date',
    remark: 'Note',
    sort: 'Sort',
    fill: 'Please enter your ',
    select: 'Please select ',
    tip: 'Prompting',
    result: 'Result',
    yes: 'Yes',
    no: 'No',
    agree: 'Agree',
    refuse: 'Refuse',
    insert_success: 'Successfully Added',
    update_success: 'Successfully Edited',
    delete_success: 'Successfully Deleted',
    operate_success: 'Successfully Operated',
    operate_error: 'Operated Failed',
    refresh_success: 'Successfully Refreshed',
    delete_confirm: 'Are you sure you want to delete the selected item(s)?',
    export_confirm: 'Are you sure you want to export the selected item(s)?',
    upload_click: 'Choose a file or drag it here',
    upload_format_excel: '.xls or .xlsx files only',
    download_confirm: 'Are you sure you want to download this item(s)?',
    name: 'Name',
    phone: 'Contact',
    mail: 'E-mail',
    country: 'Region',
    keywords: 'Search keywords',
    format: 'Please enter a valid format',
    select_all: 'Select All/Disselect All',
    each_link: 'Linking parent-child',
    loading: 'Loading…',
    enable: 'Enable',
    disable: 'Disable',
    male: 'Male',
    female: 'Female',
    before: 'Previous',
    next: 'Next',
    batch_split: ' Please use "," to separate different models'
  },
  //错误
  error: {
    tip: 'Error!',
    no_permission: 'You are not authorized to access！',
    permission_message: 'Sorry, your are not authorized to access! please return to main page.',
    no_path: 'This page could not be found！',
    path_message: 'Sorry, this page does not exist. Please check the URL or go back a page.',
    back: 'Back',
    home: 'Back to homepage'
  },
  //登录页
  login: {
    logIn: 'Login',
    username: 'Username',
    password: 'Password',
    CAPTCHA: 'Verification Code',
    remember_me: 'Remember Me',
    login: 'Login',
    logining: 'Logging...',
    logout_confirm: 'Are you sure you want to logout？'
  },
  //首页
  homepage: {
    content: 'Be pending'
  },
  //布局
  layout: {
    size: 'Layout Size',
    personal_center: 'Personal Center',
    setting: 'Layout Settings',
    logout: 'Logout'

  },
  tagsView: {
    refresh: 'Page Refresh',
    close: 'Close Tag',
    close_left: 'Close Left Tag',
    close_right: 'Close Right Tag',
    close_other: 'Close Else',
    close_all: 'Close All'
  },
  settings: {
    theme_style: 'Theme Style',
    theme_color: 'Theme Color',
    layout_settings: 'System Layout',
    TopNav: 'Enable TopNav',
    tagsView: 'Enable Tags-View',
    fix_header: 'Fix Header',
    sidebarLogo: 'Sidebar Logo',
    dynamic_title: 'Dynamic Title',
    save_settings: 'Save Configuration(s)',
    init_settings: 'Reset Configuration(s)'
  },
  //文件
  file: {
    select: 'Select file',
    special_chart: 'File names cannot contain special characters',
    success: 'Upload succeeded',
    error: 'Upload failed',
    format_error: 'The file format is incorrect. The correct format is: ',
    size_error: 'Upload file size cannot exceed: ',
    count_error: 'The number of uploaded files cannot exceed: ',
    size_tip: 'Size no more than',
    format_tip: 'File format',
  },
  //系统管理-配置
  config: {
    name: 'Param. Name',
    key: 'Key-name',
    default: 'System Default',
    value: 'Key-value',
    insert: 'Add Params.',
    update: 'Edit Params.'
  },
  //系统管理-部门
  dept: {
    name: 'Dept. Name',
    parent: 'Superior Dept.',
    leader: 'Director',
    insert: 'Add Dept.',
    update: 'Edit Dept.'
  },
  //样式类型
  style: {
    default: 'Default',
    primary: 'Primary',
    success: 'Success',
    info: 'Info',
    warning: 'Warning',
    danger: 'Danger'
  },
  //系统管理-字典
  dict: {
    name: 'Dictionary Name',
    type: 'Dictionary Type',
    inset: 'Add Dictionary Type',
    update: 'Edit Dictionary Type',
    label: 'Data Label',
    labelEn: 'Data Label(En)',
    value: 'Key-value',
    style: 'Style Properties',
    back_style: 'Echo Style',
    insert_data: 'Add Entry',
    update_data: 'Edit Dictionary'
  },
  //系统管理-菜单
  menu: {
    name: 'Menu Name',
    icon: 'Icon',
    perms: 'Permission ID',
    en_name: 'English Name',
    parent: 'Superior Menu',
    dir: 'Catalogue',
    menu: 'Menu',
    button: 'Button',
    if_link: 'Is it an external link',
    link_tip: 'If it is an external link,  it should start with `http(s)://`',
    router: 'Routing Address',
    router_tip: 'The routing address, such as `user`. If it is an external link, it should start with `http(s)://`',
    component: 'Component Path',
    component_tip: 'Component access path, e.g. `system/user/index`, default path is `views`',
    permission: 'Permission Characters',
    permission_tip: 'Permission characters defined in controller, e.g. @PreAuthorize(`@ss.hasPermi(\'system:user:list\')`)',
    param: 'Route Parameters',
    param_tip: 'Default delivery parameters for access routes, e.g. `{"id": 1, "name": "ry"}`',
    if_cache: 'Whether Cache',
    cache_tip: 'If enabled, it will be cached by `keep alive`, and `name` and ID of components need to be consistent.',
    if_show: 'Display Status',
    show_tip: 'If hid, the route will not show in the sidebar but still can be accessed',
    status: 'Menu Status',
    status_tip: 'If disabled, the route will not show in the sidebar and cannot be accessed',
    insert: 'Add Menu',
    update: 'Edit Menu'
  },
  //系统管理-公告
  notice: {
    title: 'Notice Title',
    operator: 'Operator',
    content: 'Content',
    insert: 'Add Notice',
    update: 'Edit Notice',
    tip: 'Please enter an input'
  },
  //系统管理-岗位
  post: {
    code: 'Post ID',
    name: 'Post Name',
    insert: 'Add Post',
    update: 'Edit Post'
  },
  //系统管理-人员
  user: {
    name: 'Username',
    nickname: 'Nickname',
    country: 'Region',
    countryNot: 'not Region',
    add: 'Add User',
    update: 'Edit User',
    import: 'Import Users',
    avatar: 'Change Faces',
    avatar_format: 'Format Error, please choose .JPG and .PNG files',
    info: 'Basic Info',
    base: 'Basic Profile',
    role_info: 'Role Info',
    cancel_perms: 'Cancel Authorization',
    cancel_role_confirm: 'Are you sure you want to cancel this role?',
    cancel_permission_confirm: 'Uncheck user authorization item(s)?',
    update_confirm: 'Update existing user data?',
    select: 'Please select user',
    num: 'No.',
    dept: 'Dept.',
    password: 'Password',
    old_password: 'Old Password',
    new_password: 'New Password',
    confirm_password: 'Confirm Password',
    error_password: 'Entered passwords differ!',
    reset_password: 'Reset Password',
    update_password: 'Change Password',
    sex: 'Gender',
    post: 'Post',
    role: 'Role',
    select_role: 'Assign Role',
    name_format: 'Name length must be between 2 and 20 characters',
    password_format: 'Password length must be between 2 and 20 characters'
  },
  //系统管理-角色
  role: {
    name: 'Role Name',
    permission: 'Permission',
    permission_tip: 'Authority characters defined in controller, e.g. @PreAuthorize(`@ss.hasRole(\'admin\')`)',
    data_perms: 'Access Permission',
    select_user: 'Assign Users',
    menu_perms: 'Menu Permissions',
    rang: 'Scope of Authority',
    all: 'All Access',
    custom: 'Custom Access',
    dept: 'Departmental Access',
    dept_under: 'Departmental and Subordinate Access',
    self: 'Personal Access',
    insert: 'Add Role',
    update: 'Edit Role',
    select_perms: 'Assign Access',
    select_series: 'Assign Series'
  },
  //产品系列
  base_model: {
    insert: 'Add Product Model',
    update: 'Edit Product Model',
    series: 'Product Series',
    model: 'Product Model'
  },
  //产品序列号
  base_serial: {
    create_time: 'Manufacture Date',
    deliver_time: 'Deliver Date',
    warranty: 'Warranty Period',
    user_address: 'User Address',
    insert: 'Add SN',
    update: 'Edit SN',
    import: 'Import SN',
    firmware_version: 'Firmware Version',
    order_number: 'Order Number',
    life_title: 'Life Cycle',
    operate_type: 'Operate Type',
    remark: 'Content',
    support_code: 'Order Code'
  },
  //基础表单-激活码
  base_key: {
    active_key: 'Activation Code',
    order_code: 'Order No.',
    serial: 'Serial Number',
    active_date: 'Activated Date',
    extend: 'Warranty Extended/month',
    insert: 'Add Activation Code',
    update: 'Change Activation Code',
    active_count: 'Number of Application'
  },
  //审批相关
  approve: {
    start: 'Send Requst',
    agree: 'Approve',
    refuse: 'Reject',
    start_confirm: 'Are you sure you want to send a requst with selected item(s)?',
    agree_confirm: 'Are you sure you want to approve the selected item(s)?',
    refuse_confirm: 'Are you sure you want to reject the selected item(s)?'
  },
  //换机
  exchange: {
    order_code: 'Order Code',
    fault_code: 'Alarm Code',
    fault_name: 'Alarm Name',
    fault_des: 'Alarm Description',
    address: 'End-user Address',
    electric_standard: 'Grid Standard',
    provider: 'Secondary Supplier',
    supporter: 'Technical Supporter',
    sender_name: 'Consignor',
    sender_phone: 'Phone of Consignor',
    sender_address: 'Address of Consignor',
    receiver_name: 'Consignee',
    receiver_phone: 'Phone of Consignee',
    receiver_address: 'Delivery Address',
    new_serial: 'SN of New Device',
    send_date: 'Delivery Date',
    send_warehouse: 'Delivery Warehouse',
    install_date: 'InstallDate of New Device',
    form_info: 'Switching Details',
    insert: 'Add Switching Registration',
    update: 'Edit Switching Registration',
    write_express: 'Delivery Info',
    write_new_machine: 'New Device Info',
    refuse_confirm: 'Are you sure you want to reject? The process will return to the previous node. ',
    interrupt_confirm: 'Are you sure you want to abort? The process cannot be restored.',
    exchange_agree: 'Agree to Switch',
    exchange_refuse: 'Reject to Switch',
    receive: 'Confirm Receipt',
    interrupt: 'Abort Process'
  },
  //质保
  warranty: {
    name: 'Name',
    phone: 'Phone Number',
    mail: 'E-mail',
    install_company: 'Installer',
    install_date: 'Installation Date',
    install_address: 'Installation Address',
    start_date: 'Warranty Start Date',
    duration: 'Warranty Duration',
    end_date: 'Warranty End Date',
    register_time: 'Check-in Date'
  },
  //工单
  workorder: {
    code: 'ID',
    description: 'Description',
    create_by: 'Creator',
    region: 'Region',
    urgency: 'Priority',
    type: 'Type',
    end_user: 'Enduser',
    end_user_tip: 'Please enter relevant end-user information, including address, mobile phone number, etc',
    problem_account: 'Related Enduser',
    browser: 'Browser',
    version_info: 'Version',
    function_menu: 'Function Menu',
    secret_info: 'Privacy Information',
    operate_system: 'OS',
    app_version: 'APP Version',
    problem_description: 'Description',
    file: 'Attachment',
    email: 'Email',
    email_tip: 'Up to three Emails, each separated by a comma (,)',
    remind: 'Process Reminder',

    step_category: 'Problem Category',
    step_description: 'Problem Description',
    step_submittals: 'Submittals',
    no_step: 'No previous step',
    add_confirm: 'Confirm to submit work order?',
    receive_confirm: 'Confirm to accept work order?',

    status_all: 'ALL',
    status_dispatch: 'To be Assigned',
    status_pending: 'Pending',
    status_doing: 'In Process',
    status_examine: 'To be Audited',
    status_confirm: 'To be Confirmed',
    status_close: 'Closed',
    status_auto_close: 'Closed (Timeout)',
    status_manual_close:'Closed (Manually)',

    operate_exchange: 'Change device',
    operate_dispatch: 'Assign',
    operate_receive: 'Accept',
    operate_transfer: 'Transfer',
    operate_submit: 'Reply',
    operate_examine: 'Audit',
    operate_evaluate: 'Evaluate ',
    operate_close: 'Close',

    remarks: 'Remarks',
    operator: 'Operator',
    reason: 'Diagnosis',
    resolve_mark: 'Solution',
    examine_agree: 'Approve',
    examine_refuse: 'Disapprove',
    evaluation: 'Evaluate|Suggestion',
    close_reason: 'Close Type',
    receiver: 'Receiver',
    detail: 'Details'
  },
  support: {
    receive: 'Process',
    support: 'Need support',
    supported: 'Provide advice',
    apply: 'Replacement devices',
    handle_apply: 'Process application',
    handle_examine_se: 'Validate application(SE)',
    handle_examine: 'Validate application',
    resolve: 'Output solution',
    email: 'Send email',
    extend: 'Extend',
    close: 'Close',

    priority: 'priority',
    code: 'Number',
    serial: 'Serial number',
    detail: 'Problem detail',
    type: 'Problem type',
    operator_name: 'Applicant',
    operator_time: 'Application time',
    update_time: 'Latest update time',
    remark: 'Remarks',
    source: 'Source',
    source_auto:'Auto',
    source_handle:'Handle',
    start_title:'Submit Order',
    serial_title: 'Serial Number Info',
    export_confirm: 'Are you sure to export all the currently filtered data？',
    close_confirm: 'Are you sure to close this order？',
    exchange_serial: 'Exchange SN',
    flow_chart: 'Order Flow Chart'
  },
  archives: {
    order_code: 'Order Code',
    sale_code: 'Sale Code',
    deliver_code: 'Deliver Code'
  }
}
