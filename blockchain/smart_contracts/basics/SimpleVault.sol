// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

contract SimpleVault {
    address public owner;
    mapping(address => uint256) public balances;

    event Deposited(address indexed from, uint256 amount);
    event Withdrawn(address indexed to, uint256 amount);
    event Sent(address indexed from, address indexed to, uint256 amount);

    constructor() {
        owner = msg.sender;
    }

    // 1) Accept plain ETH transfers (no data)
    receive() external payable {
        // TODO: credit msg.sender using msg.value and emit Deposited
        balances[msg.sender] += msg.value;
        emit Deposited(msg.sender, msg.value);
    }

    // 2) Explicit deposit function (when users call a function)
    function deposit() external payable {
        // TODO: require msg.value > 0; then credit and emit
        require(msg.value > 0, "No ETH sent");
        balances[msg.sender] += msg.value;
        emit Deposited(msg.sender, msg.value);
    }

    // Helper views
    function myBalance() external view returns (uint256) {
        return balances[msg.sender];
    }

    function contractBalance() external view returns (uint256) {
        return address(this).balance;
    }
}